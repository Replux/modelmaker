package cn.replux.modelmaker.processor;

import cn.replux.modelmaker.annotation.FieldMapping;
import cn.replux.modelmaker.annotation.ModelMaker;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeTranslator;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;


@SupportedAnnotationTypes("cn.replux.modelmaker.annotation.FieldMapping")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class FieldMappingProcessor extends BaseProcessor {


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        logger.debug("enter FieldMappingProcessor");

        Set<? extends Element> fieldMappings = roundEnv.getElementsAnnotatedWith(FieldMapping.class);


        TreeTranslator translator = new TreeTranslator() {
            @Override
            public void visitClassDef(JCTree.JCClassDecl classDecl) {
                logger.debug("mods:{}, typarams:{}, extendng:{}, implementing:{},defs:{}",
                        classDecl.mods,
                        classDecl.typarams,
                        classDecl.extending,
                        classDecl.implementing,
                        classDecl.defs);
            }
            @Override
            public void visitMethodDef(JCTree.JCMethodDecl methodDecl) {
                logger.debug("mods:{}, restype:{}, typarams:{}, recvparam:{},params:{},thrown:{},body:{}",
                        methodDecl.mods,
                        methodDecl.restype,
                        methodDecl.typarams,
                        methodDecl.recvparam,
                        methodDecl.params,
                        methodDecl.thrown,
                        methodDecl.body);
            }
            @Override
            public void visitVarDef(JCTree.JCVariableDecl variableDecl) {
                logger.debug("mods:{}, nameexpr:{}, vartype:{}, init:{}",
                        variableDecl.mods,
                        variableDecl.nameexpr,
                        variableDecl.vartype,
                        variableDecl.init);
            }
            @Override
            public void visitBlock(JCTree.JCBlock block) {
                logger.debug("block:{}", block);
            }

        };

        fieldMappings.forEach(element->{
            logger.debug("fieldMappings:{}",element);
            JCTree jcTree = trees.getTree(element);
            jcTree.accept(translator);
        });
        return false;
    }
}
