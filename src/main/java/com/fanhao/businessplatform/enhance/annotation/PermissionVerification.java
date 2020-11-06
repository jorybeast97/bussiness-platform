package com.fanhao.businessplatform.enhance.annotation;

import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.*;


@Target(ElementType.METHOD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionVerification {
    //默认为普通角色
    String role() default "";
}
