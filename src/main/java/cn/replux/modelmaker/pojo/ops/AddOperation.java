package cn.replux.modelmaker.pojo.ops;

public class AddOperation extends Operation {
    private String newFieldName;
    private String newFieldType;


    @Override
    public void assemble(String[] args) {

    }

    public String getNewFieldName() {
        return newFieldName;
    }

    public void setNewFieldName(String newFieldName) {
        this.newFieldName = newFieldName;
    }

    public String getNewFieldType() {
        return newFieldType;
    }

    public void setNewFieldType(String newFieldType) {
        this.newFieldType = newFieldType;
    }



}
