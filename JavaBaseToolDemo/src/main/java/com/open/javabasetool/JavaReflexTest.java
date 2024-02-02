package com.open.javabasetool;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0
 * @Author cmy
 * @Date 2024/1/19 13:55
 * @desc Java反射测试类
 */
public class JavaReflexTest {
    public static void main(String[] args) throws Exception {
        Student student = new Student();
        student.setId(123456L);
        student.setName("123");
        student.setSex(1);
        student.setIds(new Long[]{1L, 2L, 3L});
        List<User> users=new ArrayList<>();
        User user = new User();
        user.setAge(26);
        user.setName("cmy");
        users.add(user);
        student.setUsers(users);

        /**
         * Java反射机制提供了一种在运行时检查和操作类、方法和属性的能力。通过反射，我们可以在运行时获取类的信息，包括属性的名称、类型和注释等。
         * 下面的代码示例展示了如何使用Java反射获取一个实体对象的属性注释。
         */
        //Class<? extends Student> aClass = student.getClass();
        Class<?> aClass = student.getClass();
        //获取操作类
        System.out.println(aClass.getName());
        //循环获取类中定义的所有方法
        for (Method method : aClass.getMethods()) {
            ////获取单个方法
            //System.out.println("method:" + method.getName() + "");
        }
        //循环获取类中定义的所有属性及其属性值
        for (Field field : aClass.getDeclaredFields()) {
            //输出单个属性
            System.out.println("field:" + field.getName() + "");
            //如果属性是私有的，需要设置为可访问
            field.setAccessible(true);
            //输出单个属性值
            System.out.println("fieldValue:" + field.get(student) + "");
            //循环获取类中定义的所有属性注释
            for (Annotation annotation : field.getAnnotations()) {
                //输出单个属性注释
                System.out.println("annotation:" + annotation + "");
            }
        }
    }
}
