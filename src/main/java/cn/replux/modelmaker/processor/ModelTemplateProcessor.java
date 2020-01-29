package cn.replux.modelmaker.processor;


import cn.replux.modelmaker.annotation.ModelMaker;
import cn.replux.modelmaker.annotation.ModelTemplate;
import cn.replux.modelmaker.pojo.FieldDecl;
import cn.replux.modelmaker.pojo.ModelDefinition;
import cn.replux.modelmaker.pojo.ops.*;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.SymbolMetadata;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;

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
        // template -> fields
        Map<String,List<FieldDecl>> materialContainer = new HashMap<>();
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
        List<Operation> operations = getOperations(methodDecl);
        String outputPath = Optional.ofNullable(annotationArgs.get("outputPath"))
                .map(String::valueOf)
                .orElse(defaultOutputPath);
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
        return assembleModelDefinition(outputPath,characteristics,operations);
    }


    private ModelDefinition assembleModelDefinition(String outputPath, java.util.List<String> characteristics, List<Operation> operations) {
        logger.debug("outputPath:{}",outputPath);
        characteristics.forEach(c->{
            logger.debug("characteristic:{}",c);
        });
        operations.forEach(op->{
            logger.debug("operation:{}",op);
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
        String[] args = generalizeArgs(methodInvocation.args);
        if(methodInvocation.meth instanceof JCTree.JCIdent){ // 静态导入方法
            JCTree.JCIdent operator = (JCTree.JCIdent) methodInvocation.meth;
            return OperationFactory.getOperation(String.valueOf(operator.name), args);
        }else if(methodInvocation.meth instanceof JCTree.JCFieldAccess){ //全限定名
            JCTree.JCFieldAccess operator = (JCTree.JCFieldAccess) methodInvocation.meth;
            return OperationFactory.getOperation(String.valueOf(operator.name), args);
        }
        return null;
    }

    private static String[] generalizeArgs(List<JCTree.JCExpression> args) {
        //TODO:
        args.stream().map(arg->{
            if(arg instanceof JCTree.JCIdent){
                JCTree.JCIdent ident = (JCTree.JCIdent) arg;
                return String.valueOf(ident.name);
            }else if(arg instanceof JCTree.JCLiteral){
                JCTree.JCLiteral literal = (JCTree.JCLiteral) arg;
                return String.valueOf(literal.value);
            }
            return String.valueOf(arg);
        });
        return new String[0];

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

    private static void printMaterialContainer(Map<String,List<FieldDecl>> materialContainer){
        materialContainer.forEach((key,list)->{
            list.forEach(fieldDecl -> {
                logger.debug(">>> materialContainer\n modelName:{},\n FieldName:{}\nFieldType:{}",
                        key,fieldDecl.getName(),fieldDecl.getType());
            });
        });
    }

}
