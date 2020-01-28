package cn.replux.modelmaker.processor;


import cn.replux.modelmaker.annotation.ModelMaker;
import cn.replux.modelmaker.annotation.ModelTemplate;
import cn.replux.modelmaker.pojo.FieldDecl;
import cn.replux.modelmaker.pojo.operation.BaseOperation;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static cn.replux.modelmaker.processor.ProcessUtil.*;

@SupportedAnnotationTypes("cn.replux.modelmaker.annotation.ModelTemplate")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ModelTemplateProcessor extends BaseProcessor{

    // template -> fields
    private Map<String,List<FieldDecl>> materialContainer = new HashMap<>();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        logger.debug("enter ModelTemplate");

        Set<? extends Element> templates = roundEnv.getElementsAnnotatedWith(ModelTemplate.class);
        ListBuffer<JCTree.JCClassDecl> jcClassDeclList = new ListBuffer<>();
        templates.forEach(element -> {
            trees.getTree(element).accept(new TreeTranslator() {
                @Override
                public void visitClassDef(JCTree.JCClassDecl jcClass) {
                    jcClassDeclList.append(jcClass);
                    // 装配原材料(字段)
                    String className = String.valueOf(element);
                    List<FieldDecl> fieldDecls = getFieldDecls(jcClass);
                    materialContainer.put(className,fieldDecls);
                    //printMaterialContainer(materialContainer);
                }
            });
        });

        jcClassDeclList.toList().forEach(jcClass -> {
            Set<JCTree.JCMethodDecl> validMethodDecls = getValidMethodDecl(jcClass);
            validMethodDecls.forEach(methodDecl -> {
                Map<String,JCTree.JCExpression> args = getAnnotationArgs(methodDecl);
                JCTree.JCExpression birthPlace = args.get("birthPlace");
                JCTree.JCExpression characteristics = args.get("characteristics");
                logger.debug("birthPlace:{},characteristics:{}",
                        birthPlace,characteristics);
                getOperations(methodDecl);
            });


        });
        return true;


    }

    private static List<BaseOperation> getOperations(JCTree.JCMethodDecl methodDecl) {
        ListBuffer<BaseOperation> operations= new ListBuffer<>();
        JCTree.JCBlock body = methodDecl.body;
        List<JCTree.JCStatement> stats = body.stats;
        stats.forEach(stat->{
            JCTree.JCExpression expr = ((JCTree.JCExpressionStatement) stat).expr;
            if(expr instanceof JCTree.JCMethodInvocation){
                JCTree.JCMethodInvocation methodInvocation = (JCTree.JCMethodInvocation) expr;
                logger.debug("methodInvocation:{}",methodInvocation);
                if(methodInvocation.meth instanceof JCTree.JCIdent){
                    JCTree.JCIdent operator = (JCTree.JCIdent) methodInvocation.meth;
                    //TODO: 根据operator来解析args
                    List<JCTree.JCExpression> args = methodInvocation.args;
                    args.forEach(arg->{
                        logger.debug("arg.getClass:{}",arg.getClass());
                        logger.debug("meth:{},args:{}",methodInvocation.meth,arg);
                    });
                }
            }

        });
        return operations.toList();
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
