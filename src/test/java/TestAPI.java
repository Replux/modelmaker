
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
        HashMap<String, String> map= new HashMap<String, String>(){
            {
                put("a","1");
                put("b","2");
                put("c","3");
            }
        };

        HashMap<String, String> map2 = new HashMap<>(map);
        map2.remove("a");
        System.out.println(map.equals(map2));

    }
}
