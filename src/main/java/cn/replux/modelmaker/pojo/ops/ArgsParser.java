package cn.replux.modelmaker.pojo.ops;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.List;

@FunctionalInterface
public interface ArgsParser {

    void parse(List<JCTree.JCExpression> args);
}
