package cn.replux.modelmaker.pojo.ops;


import java.util.List;

public abstract class Operation {
    private OperationType flag;

    final public OperationType getFlag() {
        return flag;
    }

    final public void setFlag(OperationType flag) {
        this.flag = flag;
    }

    abstract void assemble(List<String> args);
}
