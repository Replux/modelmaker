package cn.replux.modelmaker.pojo.ops;


import java.util.List;

public class OperationFactory {
    public static Operation getOperation(String name, List<String> args){
        try {
            Operation operation;
            switch (OperationType.valueOf(name.toUpperCase())){
                case TRANSLATE:
                    operation = new TranslateOperation();
                    break;
                case ADD:
                    operation = new AddOperation();
                    break;
                case REDUCE:
                    operation = new ReduceOperation();
                    break;
                default:
                    return null;
            }
            operation.assemble(args);
            return operation;
        } catch (IllegalArgumentException e) {
            // No matching OperationType
            return null;
        }

    }
}
