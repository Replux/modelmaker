package cn.replux.modelmaker.processor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommonUtil {
    static final Logger logger = LogManager.getLogger(CommonUtil.class);

    static Class<?> getClz(String type) {
        Class<?> basicClass = getBasicClass(type);
        if(basicClass!=null){
            return basicClass;
        }

        try {
            return Class.forName(type);
        } catch (ClassNotFoundException e) {
            logger.debug("class["+type+"] does not exist");
        }
        return null;
    }

    private static Class<?> getBasicClass(String type) {
        switch (type){
            case "String":
            case "string":
                return String.class;
            //---
            case "Integer":
                return Integer.class;
            case "int":
                return int.class;
            //---
            case "long":
                return long.class;
            case "Long":
                return Long.class;
            //---
            case "byte":
                return byte.class;
            case "Byte":
                return Byte.class;
            //---
            case "short":
                return short.class;
            case "Short":
                return Short.class;
            //---
            case "float":
                return float.class;
            case "Float":
                return Float.class;
            //---
            case "double":
                return double.class;
            case "Double":
                return Double.class;
            //---
            case "boolean":
                return boolean.class;
            case "Boolean":
                return Boolean.class;
            //---
            case "char":
                return char.class;
            case "character":
            case "Character":
                return Character.class;
            //---
            case "object":
            case "Object":
                return Object.class;
            default:
                return null;
        }
    }
}
