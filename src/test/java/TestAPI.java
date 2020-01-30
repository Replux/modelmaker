
import cn.replux.modelmaker.Operator;

import cn.replux.modelmaker.pojo.ops.*;
import cn.replux.modelmaker.pojo.ops.OperationType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static cn.replux.modelmaker.Operator.*;

public class TestAPI {

    public static void main(String[] args) {
        try {
            Class aClass = Class.forName("java.lang.String");
            System.out.println(aClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
