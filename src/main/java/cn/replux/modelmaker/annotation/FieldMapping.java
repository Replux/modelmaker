package cn.replux.modelmaker.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER}) // 使用在类上
@Retention(RetentionPolicy.SOURCE)
public @interface FieldMapping {
    String from();
    String toName() default "";
    Class toType();
}
