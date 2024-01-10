package com.open.javabasetool;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.DiffResult;

/**
 * @version 1.0
 * @Author cmy
 * @Date 2024/1/10 21:45
 * @desc CommonLang的对象比较器DiffBuilder
 */

@Slf4j
public class ObjectDiffTest {
    /**
     * Common Lang 中的 Builder 包内提供了一个 DiffBuilder 类，可以比较两个对象，并返回不同的部分。
     * 首先在要比较对象的类中实现 Diffable 接口，然后实现 DiffResult diff(T obj)  方法。
     * 在DiffResult diff(T obj)  方法中，新建一个 DiffBuilder 对象，把需要比较的类属性一一放入 DiffBuilder 中。
     *
     * 链接：
     * https://juejin.cn/post/7033394535736541198
     * https://blog.csdn.net/feeltouch/article/details/86683119
     * @param args
     */
    public static void main(String[] args) {
        Student student = new Student();
        student.setName("123");
        student.setSex(1);
        User user1 = new User();
        user1.setName("456");
        user1.setAge(22);
        User user2 = new User();
        user2.setName("789");
        user2.setAge(23);

        DiffResult result = user1.diff(user2);
        log.info("result -> {}", result);
    }
}
