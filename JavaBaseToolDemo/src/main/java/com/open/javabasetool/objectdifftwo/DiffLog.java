package com.open.javabasetool.objectdifftwo;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 对比日志注解DiffLog
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DiffLog {
    /**
     * 汉字全称
     * @return name
     */
    String name();

    /**
     * Date 如何格式化，默认可以为空
     * @return dateFormat
     */
    String dateFormat() default "";

    /**
     * 字典枚举类 默认空值Integer.class
     */
    Class dictEnum() default Integer.class;

    /**
     * 是否忽略该值
     * @return ignore
     */
    boolean ignore() default false;
}
