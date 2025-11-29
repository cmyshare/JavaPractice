package com.open.objectdifftwo;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 对比日志注解DiffLog
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DiffLog {
    /**
     * 汉字全称
     *
     * @return name
     */
    String name();

    /**
     * Date 如何格式化，默认可以为空
     *
     * @return dateFormat
     */
    String dateFormat() default "";

    /**
     * 字典枚举类 默认空值Integer.class 枚举类必有getNameByType方法
     */
    Class dictEnum() default Integer.class;

    /**
     * 是否忽略该值 默认否
     *
     * @return ignore
     */
    boolean ignore() default false;

    /**
     * 嵌套集合是否输出对比 默认是
     *
     * @return ignore
     */
    boolean nestedCollect() default true;
}
