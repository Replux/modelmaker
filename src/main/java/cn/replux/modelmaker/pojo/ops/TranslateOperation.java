package cn.replux.modelmaker.pojo.ops;


import java.util.List;

public class TranslateOperation extends Operation {
    private String oldFieldName;
    private String newFieldName;
    private String newFieldType;

    @Override
    public void assemble(List<String> args) {
        if(args.size()==2){
            oldFieldName=args.get(0);
            newFieldName=args.get(1);
        }else if(args.size()==3) {
            oldFieldName=args.get(0);
            newFieldName=args.get(1);
            newFieldType=args.get(2);
        }
    }

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
