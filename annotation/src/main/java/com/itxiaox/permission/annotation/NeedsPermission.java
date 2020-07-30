package com.itxiaox.permission.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用户获得权限后，处理。。。
 */
@Target(ElementType.METHOD)//该方法注解作用在方法之上
@Retention(RetentionPolicy.CLASS)//在源码和class文件中都存在，编译器注解方式
public @interface NeedsPermission {

}
