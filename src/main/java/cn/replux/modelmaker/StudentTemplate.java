package cn.replux.modelmaker;

import cn.replux.modelmaker.annotation.FieldMapping;
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

    @ModelMaker(outputPath = "cn.replux.model",
            characteristics = {"Data","Builder"})
    void StudentDO(){
        @FieldMapping int i;

        translate(name,"name","String");
        translate(age,"age","int");
        add("height","Integer");
        reduce(grade);
    }


    @ModelMaker
    void StudentVO(){
        Operator.translate(name,"mingzi","java.lang.String");
        cn.replux.modelmaker.Operator.translate(age,"nianling","long");
        add("height","Integer");
    }

}
