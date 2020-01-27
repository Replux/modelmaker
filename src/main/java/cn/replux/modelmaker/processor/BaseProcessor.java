package cn.replux.modelmaker.processor;

import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Names;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;


public abstract class BaseProcessor extends AbstractProcessor {

    static final Logger logger = LogManager.getLogger(BaseProcessor.class);
    Messager messager; //用于在编译器打印消息的组件
    JavacTrees trees; //语法树
    TreeMaker treeMaker; //用来构造语法树节点
    Names names;


    /**
     * 获取一些注解处理器执行处理逻辑时需要用到的一些关键对象
     * @param processingEnv 处理环境
     */
    @Override
    public final synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.messager = processingEnv.getMessager();
        this.trees = JavacTrees.instance(processingEnv);
        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        this.treeMaker = TreeMaker.instance(context);
        this.names = Names.instance(context);
    }
}
