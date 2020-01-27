import cn.replux.modelmaker.annotation.FieldMapping;
import cn.replux.modelmaker.annotation.ModelMaker;
import cn.replux.modelmaker.annotation.ModelTemplate;

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

    @ModelMaker(mapper = {
            @FieldMapping(from = "name", toName = "mingzi", toType = String.class),
            @FieldMapping(from = "age", toName = "nianling", toType = Integer.class),
            @FieldMapping(from = "male", toName = "xingbie", toType = Boolean.class),
    }) void StudentDTO(){}

    @ModelMaker(mapper = {
            @FieldMapping(from = "name", toType = String.class),
            @FieldMapping(from = "age", toType = Integer.class),
            @FieldMapping(from = "male", toType = Boolean.class),
    }) void StudentVO(){}

}
