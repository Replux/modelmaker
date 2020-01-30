package cn.replux.modelmaker.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 1.9SNAP-SHOT主要是模仿lombok写个demo以熟悉下JCTREE的API
 *
 * @author replux(杨哲庆)
 * @since 2020-1-26 22:09:46
 * GitHub: https://github.com/Replux
 * email: ALU1948@outlook.com, 415134023@qq.com
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface ModelMaker {

    String packageName() default "";
    String[] characteristics() default {"Data"};
}
