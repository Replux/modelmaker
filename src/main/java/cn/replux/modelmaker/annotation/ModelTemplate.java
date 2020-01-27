package cn.replux.modelmaker.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE}) // 使用在类上
@Retention(RetentionPolicy.SOURCE)
public @interface ModelTemplate {
}
