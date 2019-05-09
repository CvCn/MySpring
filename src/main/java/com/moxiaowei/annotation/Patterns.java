package com.moxiaowei.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Patterns {
    Path paths() default @Path();
    ExcludePath excludePaths() default @ExcludePath();
}
