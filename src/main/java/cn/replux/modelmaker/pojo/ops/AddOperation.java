package cn.replux.modelmaker.pojo.ops;

import java.util.List;

public class AddOperation extends Operation {
    private String newFieldName;
    private String newFieldType;


    @Override
    public void assemble(List<String> args) {
        if(args.size()==2){
            newFieldName = args.get(0);
            newFieldType = args.get(1);
        }
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
