package cn.replux.modelmaker;

import cn.replux.modelmaker.annotation.ModelMaker;
import cn.replux.modelmaker.annotation.ModelTemplate;

import static cn.replux.modelmaker.Operator.*;

@ModelTemplate
public class StudentTemplate {

    // ______________ material ______________

    Long id;
    String name;
    int age;
    boolean male;
    int grade;

    // ______________ entities ______________

    @ModelMaker(packageName = "cn.replux.model",
            characteristics = {"Data","Builder"})
    void StudentDO(){
        translate(name,"name","String");
        translate(age,"age","cn.replux.modelmaker.Operator");
        add("height",Integer.class);
        add("height","Integer");
        //reduce(grade);
    }


    @ModelMaker
    void StudentVO(){
        Operator.translate(name,"mingzi","java.lang.String");
        cn.replux.modelmaker.Operator.translate(age,"nianling","long");
        //add("height","Integer");
    }

}
