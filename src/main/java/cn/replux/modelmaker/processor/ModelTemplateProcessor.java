package cn.replux.modelmaker.processor;


import cn.replux.modelmaker.annotation.ModelMaker;
import cn.replux.modelmaker.annotation.ModelTemplate;
import cn.replux.modelmaker.pojo.FieldDecl;
import cn.replux.modelmaker.pojo.ModelDefinition;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.TypeTag;
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
                getScript(methodDecl);
            });


        });
        return true;


    }

    private static void getScript(JCTree.JCMethodDecl methodDecl) {

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
                    JCTree.JCAssign assign = (JCTree.JCAssign) arg;
                    map.put(String.valueOf(assign.lhs),assign.rhs);
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
