import cn.replux.modelmaker.annotation.FieldMapping;
import cn.replux.modelmaker.annotation.ModelMaker;
import cn.replux.modelmaker.annotation.ModelTemplate;

import static cn.replux.modelmaker.FieldMapping.mapping;

@ModelTemplate
public class StudentTemplate {

    // ______________ material ______________

    Long id;
    String name;
    int age;
    boolean male;
    int grade;

    // ______________ entities ______________
    @ModelMaker(
            birthPlace = "cn.replux.model",
            characteristic = {"Data","Builder"},
            mapper = {
            @FieldMapping(from = "name", toName = "name", toType = String.class),
            @FieldMapping(from = "age", toName = "age", toType = Integer.class),
            @FieldMapping(from = "male", toName = "male", toType = Boolean.class),
            @FieldMapping(from = "grade", toName = "grade", toType = Integer.class)
    }) void StudentDO(){}


    @ModelMaker(birthPlace = "cn.replux.model")
    void StudentVO(){
        String mingzi  = mapping(name,String.class);
        Long nianling  = mapping(name,Long.class);
    }

}
