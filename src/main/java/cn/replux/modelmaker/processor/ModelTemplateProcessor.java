package cn.replux.modelmaker.processor;


import cn.replux.modelmaker.annotation.ModelMaker;
import cn.replux.modelmaker.annotation.ModelTemplate;
import cn.replux.modelmaker.pojo.ModelDefinition;
import cn.replux.modelmaker.pojo.ops.*;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVisitor;
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
        //ListBuffer<JCTree.JCClassDecl> jcClassDeclList = new ListBuffer<>();
        Map<String,JCTree.JCClassDecl> classDecls = new HashMap<>();
        templates.forEach(template -> {
            Symbol.ClassSymbol element = (Symbol.ClassSymbol) template;
            logger.debug("element.fullname:{}",element.fullname);

            trees.getTree(template).accept(new TreeTranslator() {
                @Override
                public void visitClassDef(JCTree.JCClassDecl jcClass) {
                    String qualifiedName = String.valueOf(template);
                    classDecls.put(qualifiedName,jcClass);
                    materialContainer.put(qualifiedName,getFieldDecls(jcClass));
                    //printMaterialContainer(materialContainer);
                }
            });
        });

        try {
            classDecls.forEach((qualifiedName,jcClass) -> {
                Set<JCTree.JCMethodDecl> validMethodDecls = getValidMethodDecl(jcClass);
                validMethodDecls.forEach(methodDecl -> {
                    String defaultOutputPath = qualifiedName.substring(0, qualifiedName.lastIndexOf("."));
                    ModelDefinition definition = convertToModelDefinition(defaultOutputPath,methodDecl);
                });
            });
        } catch (Exception e) {
            //TODO: delete trycatch
            logger.debug(e);
        }
        return true;


    }

    private ModelDefinition convertToModelDefinition(String defaultOutputPath,JCTree.JCMethodDecl methodDecl) {
        Map<String,JCTree.JCExpression> annotationArgs = getAnnotationArgs(methodDecl);

        java.util.List<String> characteristics = Optional.ofNullable(annotationArgs.get("characteristics")).map(c -> {
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

        String outputPath = Optional.ofNullable(annotationArgs.get("outputPath"))
                .map(String::valueOf)
                .orElse(defaultOutputPath);

        List<Operation> operations = getOperations(methodDecl);
        Map<String, String> newFieldDecls = executeOperations(operations);

        return assembleModelDefinition(outputPath,characteristics,newFieldDecls);
    }

    private Map<String,String> executeOperations(List<Operation> operations) {
        //TODO:
        return null;
    }


    private ModelDefinition assembleModelDefinition(String outputPath,
                                                    java.util.List<String> characteristics,
                                                    Map<String, String> newFieldDecls) {
        //TODO:
        logger.debug("outputPath:{}",outputPath);
        characteristics.forEach(c->{
            logger.debug("characteristic:{}",c);
        });
        return null;
    }

    private static List<Operation> getOperations(JCTree.JCMethodDecl methodDecl) {
        ListBuffer<Operation> operations= new ListBuffer<>();
        JCTree.JCBlock body = methodDecl.body;
        List<JCTree.JCStatement> stats = body.stats;
        stats.forEach(stat->{
            JCTree.JCExpression expr = ((JCTree.JCExpressionStatement) stat).expr;
            if(expr instanceof JCTree.JCMethodInvocation){
                Operation operation = getOperation((JCTree.JCMethodInvocation) expr);
                Optional.ofNullable(operation)
                        .ifPresent(operations::append);
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
                    if(arg instanceof JCTree.JCAssign){
                        JCTree.JCAssign assign = (JCTree.JCAssign) arg;
                        map.put(String.valueOf(assign.lhs),assign.rhs);
                    }
                });
            }
        });
        return map;
    }

//    private static void printMaterialContainer(Map<String,Map<String,String>> materialContainer){
//        materialContainer.forEach((key,list)->{
//            list.forEach(fieldDecl -> {
//                logger.debug(">>> materialContainer\n modelName:{},\n FieldName:{}\nFieldType:{}",
//                        key,fieldDecl.getName(),fieldDecl.getType());
//            });
//        });
//    }

}
