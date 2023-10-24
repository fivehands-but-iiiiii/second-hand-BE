package com.team5.secondhand.global.auth;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.PARAMETER})
public @interface Logined {
    String value() default "";
    boolean required() default true;
}
