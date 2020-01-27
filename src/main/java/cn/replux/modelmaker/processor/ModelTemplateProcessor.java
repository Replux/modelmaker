package cn.replux.modelmaker.processor;


import cn.replux.modelmaker.annotation.Data;
import cn.replux.modelmaker.annotation.ModelMaker;
import cn.replux.modelmaker.annotation.ModelTemplate;
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
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static cn.replux.modelmaker.processor.ProcessUtil.*;
import static cn.replux.modelmaker.processor.ProcessUtil.getJCVariables;

@SupportedAnnotationTypes("cn.replux.modelmaker.annotation.ModelTemplate")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ModelTemplateProcessor extends BaseProcessor{

    // template -> fields
    Map<String,List<JCTree.JCVariableDecl>> materialContainer = new HashMap<>();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        logger.debug("enter ModelTemplate");

        Set<? extends Element> templates = roundEnv.getElementsAnnotatedWith(ModelTemplate.class);

        templates.forEach(element -> {
            trees.getTree(element).accept(new TreeTranslator() {
                @Override
                public void visitClassDef(JCTree.JCClassDecl jcClass) {
                    String className = element.toString();
                    List<JCTree.JCVariableDecl> jcVariables = getJCVariables(jcClass);
                    materialContainer.put(className,jcVariables);
                }
            });
        });

        return true;


    }

    /**
     * 根据字段的语法树节点，创建对应的set方法
     *
     * @param jcVariable 字段的语法树节点
     * @return set方法的语法树节点
     */
    private JCTree.JCMethodDecl createSetJCMethod(JCTree.JCVariableDecl jcVariable) {

        ListBuffer<JCTree.JCStatement> jcStatements = new ListBuffer<>();

        //添加语句 " this.xxx = xxx; "
        jcStatements.append(
                treeMaker.Exec(
                        treeMaker.Assign(
                                treeMaker.Select(
                                        treeMaker.Ident(names.fromString(THIS)),
                                        jcVariable.name
                                ),
                                treeMaker.Ident(jcVariable.name)
                        )
                )
        );

        JCTree.JCBlock jcBlock = treeMaker.Block(
                0 //访问标志
                , jcStatements.toList() //所有的语句
        );

        return treeMaker.MethodDef(
                treeMaker.Modifiers(Flags.PUBLIC), //访问标志
                names.fromString(fromPropertyNameToSetMethodName(jcVariable.name.toString())), //名字
                treeMaker.TypeIdent(TypeTag.VOID), //返回类型
                List.nil(), //泛型形参列表
                List.of(cloneJCVariableAsParam(treeMaker, jcVariable)), //参数列表
                List.nil(), //异常列表
                jcBlock, //方法体
                null //默认方法（可能是interface中的那个default）
        );
    }

}
