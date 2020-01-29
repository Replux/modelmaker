package cn.replux.modelmaker.pojo.ops;


public abstract class Operation {
    private OperationType flag;

    final public OperationType getFlag() {
        return flag;
    }

    final public void setFlag(OperationType flag) {
        this.flag = flag;
    }

    abstract public void assemble(String[] args);
}
