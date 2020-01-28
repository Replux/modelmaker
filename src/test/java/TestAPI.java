import cn.replux.modelmaker.annotation.ModelMaker;

import java.lang.reflect.Type;

public class TestAPI {
    public static void main(String[] args) {
        if(Object.class.equals(Class.class.getGenericSuperclass())){
            System.out.println(true);
        }

        System.out.println(ModelMaker.class.getTypeName());
    }
}
