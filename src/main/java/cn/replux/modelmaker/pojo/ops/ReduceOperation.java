package cn.replux.modelmaker.pojo.ops;

public class ReduceOperation extends Operation {
    private String fieldName;


    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }


    @Override
    public void assemble(String[] args) {

    }
}
