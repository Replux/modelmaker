import cn.replux.modelmaker.annotation.ModelMaker;
import cn.replux.modelmaker.annotation.ModelTemplate;

import static cn.replux.modelmaker.Operator.add;
import static cn.replux.modelmaker.Operator.translate;

@ModelTemplate
public class StudentTemplate {

    // ______________ material ______________

    Long id;
    String name;
    int age;
    boolean male;
    int grade;

    // ______________ entities ______________
    @ModelMaker(birthPlace = "cn.replux.model",
            characteristics = {"Data","Builder"})
    void StudentDO(){
        translate(name,"name",String.class);
        translate(age,"age",long.class);
        translate(male,"male",long.class);
        translate(grade,"grade",long.class);
        add("height",Integer.class);
    }


    @ModelMaker
    void StudentVO(){
        translate(name,"mingzi",String.class);
        translate(age,"nianling",long.class);
        add("height",Integer.class);
    }

}
