package com.open.javabasetool.objectdifftwo;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 对比日志主键注解DiffLogKey
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DiffLogKey {
    String name() default "id";
}
