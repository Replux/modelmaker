package cn.replux.modelmaker.annotation;

public @interface FieldMapping {
    String from();
    String toName() default "";
    Class toType();
}
