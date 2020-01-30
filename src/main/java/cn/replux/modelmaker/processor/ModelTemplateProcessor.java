package cn.replux.modelmaker.processor;


import cn.replux.modelmaker.annotation.ModelMaker;
import cn.replux.modelmaker.annotation.ModelTemplate;
import cn.replux.modelmaker.pojo.ModelDefinition;
import cn.replux.modelmaker.pojo.ops.*;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import lombok.Data;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static cn.replux.modelmaker.processor.ProcessUtil.*;

@SupportedAnnotationTypes("cn.replux.modelmaker.annotation.ModelTemplate")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ModelTemplateProcessor extends BaseProcessor{

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // template -> fieldDecls
        Map<String,Map<String,String>> materialContainer = new HashMap<>();
        Set<? extends Element> templates = roundEnv.getElementsAnnotatedWith(ModelTemplate.class);
        Map<String,JCTree.JCClassDecl> classDecls = new HashMap<>();
        templates.forEach(template -> {
            trees.getTree(template).accept(new TreeTranslator() {
                @Override
                public void visitClassDef(JCTree.JCClassDecl jcClass) {
                    String qualifiedName = String.valueOf(template);
                    classDecls.put(qualifiedName,jcClass);
                    materialContainer.put(qualifiedName,getFieldDecls(jcClass));
                }
            });
        });

        doProcess(classDecls,materialContainer);
        return true;
    }

    private void doProcess(Map<String, JCTree.JCClassDecl> classDecls, Map<String, Map<String, String>> materialContainer) {
        java.util.List<ModelDefinition> definitions = new ArrayList<>();
        classDecls.forEach((qualifiedName,jcClass) -> {
            Set<JCTree.JCMethodDecl> validMethodDecls = getValidMethodDecl(jcClass);
            validMethodDecls.forEach(methodDecl -> {
                String defaultOutputPath = qualifiedName.substring(0, qualifiedName.lastIndexOf("."));
                Map<String, String> fieldDeclsOfTemplate = materialContainer.get(qualifiedName);
                ModelDefinition definition = convertToModelDefinition(fieldDeclsOfTemplate,defaultOutputPath,methodDecl);
                definitions.add(definition);
            });
        });

        try {
            //由于要创建多个文件，所以这里可能要开多线程统一生产models
            produceModels(definitions);
        } catch (Exception e) {
            logger.debug(e);
        }
    }

    //未来可能实现并发执行
    private void produceModels(java.util.List<ModelDefinition> definitions) {
        definitions.forEach(definition->{
            String modelName = definition.getName();
            String packageName = definition.getOutputPath();

            Map<String, String> fieldDecls = definition.getFieldDecls();

            ArrayList<FieldSpec> fieldSpecs = JavaPoetUtil.createFieldSepcs(fieldDecls);
            //TODO 特性/注解解析
            TypeSpec typeSpec = TypeSpec.classBuilder(modelName)
                                        .addModifiers(Modifier.PUBLIC)
                                        .addAnnotation(AnnotationSpec.builder(Data.class).build())
                                        .addFields(fieldSpecs)
                                        .build();

            JavaFile javaFile = JavaFile.builder(packageName, typeSpec).build();
            try {
                javaFile.writeTo(this.filer);
            } catch (IOException e) {
                logger.debug("produce model fail:"+e);
            }

        });
    }

    private ModelDefinition convertToModelDefinition(Map<String, String> fieldDeclsOfTemplate,
                                                     String defaultOutputPath,
                                                     JCTree.JCMethodDecl methodDecl) {
        Map<String,JCTree.JCExpression> annotationArgs = getAnnotationArgs(methodDecl);
        java.util.List<String> characteristics = pickListFromJCNewArray(annotationArgs.get("characteristics"));
        String outputPath = Optional.ofNullable(annotationArgs.get("packageName"))
                .map(literal->{
                    if(literal instanceof JCTree.JCLiteral){
                        JCTree.JCLiteral packageName = (JCTree.JCLiteral) literal;
                        return String.valueOf(packageName.value);
                    }
                    return String.valueOf(literal);
                })
                .orElse(defaultOutputPath);

        List<Operation> operations = getOperations(methodDecl);
        String modelName = String.valueOf(methodDecl.getName());
        Map<String, String> newFieldDecls = executeOperations(fieldDeclsOfTemplate,operations);

        return assembleModelDefinition(modelName,outputPath,characteristics,newFieldDecls);
    }

    private java.util.List<String> pickListFromJCNewArray(JCTree.JCExpression jcNewArray) {
        return Optional.ofNullable(jcNewArray).map(c -> {
            if (c instanceof JCTree.JCNewArray) {
                JCTree.JCNewArray array = (JCTree.JCNewArray) c;
                return Optional.ofNullable(array.elems).orElse(List.nil())
                        .stream()
                        .map(elem -> {
                            if (elem instanceof JCTree.JCLiteral) {
                                return String.valueOf(((JCTree.JCLiteral) elem).value);
                            } else {
                                return "";
                            }
                        }).collect(Collectors.toList());
            } else {
                return new ArrayList<String>();
            }
        }).orElse(new ArrayList<>());
    }

    private Map<String,String> executeOperations(Map<String, String> fieldDeclsOfTemplate,List<Operation> operations) {
        HashMap<String, String> newFieldDecls = new HashMap<>(fieldDeclsOfTemplate);

        operations.forEach(op->{
            if(op instanceof TranslateOperation){
                TranslateOperation translateOperation = (TranslateOperation) op;
                String oldFieldName = translateOperation.getOldFieldName();
                String newFieldName = translateOperation.getNewFieldName();
                String newFieldType = translateOperation.getNewFieldType();
                if(oldFieldName.equals(newFieldName)){
                    newFieldDecls.put(oldFieldName,newFieldType);
                }else {
                    newFieldDecls.remove(oldFieldName);
                    newFieldDecls.put(newFieldName,newFieldType);
                }
            }else if(op instanceof AddOperation){
                AddOperation addOperation = (AddOperation) op;
                String newFieldName = addOperation.getNewFieldName();
                String newFieldType = addOperation.getNewFieldType();
                newFieldDecls.put(newFieldName,newFieldType);
            }else if(op instanceof ReduceOperation){
                ReduceOperation reduceOperation = (ReduceOperation) op;
                String fieldName = reduceOperation.getFieldName();
                newFieldDecls.remove(fieldName);
            }
        });
        newFieldDecls.remove(null);
        return newFieldDecls;
    }

    private ModelDefinition assembleModelDefinition(
            String modelName,
            String outputPath,
            java.util.List<String> characteristics,
            Map<String, String> newFieldDecls
    ) {
        //TODO:这里要对各个属性做合法性校验
        ModelDefinition definition = new ModelDefinition();
        definition.setName(modelName);
        definition.setOutputPath(outputPath);
        definition.setCharacteristics(characteristics);
        definition.setFieldDecls(newFieldDecls);
        return definition;
    }

    private static List<Operation> getOperations(JCTree.JCMethodDecl methodDecl) {
        ListBuffer<Operation> operations= new ListBuffer<>();
        List<JCTree.JCStatement> stats = methodDecl.body.stats;
        stats.forEach(stat->{
            JCTree.JCExpression expr = ((JCTree.JCExpressionStatement) stat).expr;
            if(expr instanceof JCTree.JCMethodInvocation){
                Operation operation = getOperation((JCTree.JCMethodInvocation) expr);
                Optional.ofNullable(operation).ifPresent(operations::append);
            }
        });
        return operations.toList();
    }

    //每个methodInvocation对应一个operation
    private static Operation getOperation(JCTree.JCMethodInvocation methodInvocation){
        java.util.List<String> args = generalizeArgs(methodInvocation.args);
        if(methodInvocation.meth instanceof JCTree.JCIdent){ // 静态导入方法
            JCTree.JCIdent operator = (JCTree.JCIdent) methodInvocation.meth;
            return OperationFactory.getOperation(String.valueOf(operator.name), args);
        }else if(methodInvocation.meth instanceof JCTree.JCFieldAccess){ //全限定名
            JCTree.JCFieldAccess operator = (JCTree.JCFieldAccess) methodInvocation.meth;
            return OperationFactory.getOperation(String.valueOf(operator.name), args);
        }
        return null;
    }

    private static java.util.List<String> generalizeArgs(List<JCTree.JCExpression> args) {
        return args.stream().map(arg -> {
            if (arg instanceof JCTree.JCIdent) {
                JCTree.JCIdent ident = (JCTree.JCIdent) arg;
                return String.valueOf(ident.name);
            } else if (arg instanceof JCTree.JCLiteral) {
                JCTree.JCLiteral literal = (JCTree.JCLiteral) arg;
                return String.valueOf(literal.value);
            }
            return String.valueOf(arg);
        }).collect(Collectors.toList());
    }

    /**
     * @param methodDecl
     * @return attributeName->attributeValue
     */
    private static Map<String,JCTree.JCExpression> getAnnotationArgs(JCTree.JCMethodDecl methodDecl) {
        Map<String,JCTree.JCExpression> map = new HashMap<>();
        List<JCTree.JCAnnotation> annotations = methodDecl.mods.annotations;
        annotations.forEach(annotation->{
            if(ModelMaker.class.getTypeName().equals(String.valueOf(annotation.type))){
                annotation.args.forEach(arg->{
                    logger.debug("arg:{}",arg);
                    if(arg instanceof JCTree.JCAssign){
                        JCTree.JCAssign assign = (JCTree.JCAssign) arg;
                        map.put(String.valueOf(assign.lhs),assign.rhs);
                    }
                });
            }
        });
        return map;
    }

}
