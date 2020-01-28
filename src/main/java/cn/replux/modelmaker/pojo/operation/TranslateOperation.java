package cn.replux.modelmaker.pojo.operation;

import cn.replux.modelmaker.pojo.operation.BaseOperation;

public class TranslateOperation extends BaseOperation {
    private String oldFieldName;
    private String newFieldName;
    private String newFieldType;

    public String getOldFieldName() {
        return oldFieldName;
    }

    public void setOldFieldName(String oldFieldName) {
        this.oldFieldName = oldFieldName;
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
