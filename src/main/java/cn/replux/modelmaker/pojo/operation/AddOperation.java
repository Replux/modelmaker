package cn.replux.modelmaker.pojo.operation;

public class AddOperation extends BaseOperation{
    private String newFieldName;
    private String newFieldType;

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
