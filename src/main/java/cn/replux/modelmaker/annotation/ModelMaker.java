package cn.replux.modelmaker.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Target;


/**
 * @author replux(杨哲庆)
 * @since 2020-1-26 22:09:46
 * GitHub: https://github.com/Replux
 * email: ALU1948@outlook.com, 415134023@qq.com
 */
@Target(ElementType.METHOD)
public @interface ModelMaker {

    String birthPlace() default "";
    FieldMapping[] mapper();
    String[] characteristic() default {"Data"};
}
