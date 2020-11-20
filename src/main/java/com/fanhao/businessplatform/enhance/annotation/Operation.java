package com.fanhao.businessplatform.enhance.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Operation {
    String operation() default "";
}
