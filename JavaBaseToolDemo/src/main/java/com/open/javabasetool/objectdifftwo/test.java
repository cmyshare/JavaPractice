package com.open.javabasetool.objectdifftwo;

import com.open.javabasetool.objectdifftwo.model.BeanB;
import com.open.javabasetool.objectdifftwo.model.BeanC;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @version 1.0
 * @Author cmy
 * @Date 2024/10/10 15:19
 * @desc
 */
public class test {
    public static void main(String[] args) throws Exception {
        BeanB beanB = new BeanB();
        beanB.setId(1L);
        beanB.setName("Airport");
        beanB.setStartDate(new Date());
        beanB.setPrice(new BigDecimal("100"));
        beanB.setConsumptionLimit(true);
        beanB.setDiscount(0.9);
        List<BeanC> listC2 = new ArrayList<>();
        BeanC beanC2 = new BeanC();
        beanC2.setBeanD(null);
        beanC2.setCid(2L);
        beanC2.setCname("2C");
        beanC2.setStartDate(new Date());
        listC2.add(beanC2);
        beanB.setBeanCList(listC2);

        //Field[] fields = BeanB.class.getDeclaredFields();
        //for (Field field : fields) {
        //    field.setAccessible(true);
        //    if (field.getName().equals("beanCList")) {
        //        field.set(o, null);
        //        break;
        //    }
        //}

        try {
            Class<?> clazz = beanB.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(DiffLog.class)) {
                    DiffLog annotation = field.getAnnotation(DiffLog.class);
                    if (!annotation.nestedCollect()) {
                        field.set(beanB, null);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("ID: " + beanB.getId());
        System.out.println("Name: " + beanB.getName());
        System.out.println("Start Date: " + beanB.getStartDate());
        System.out.println("Price: " + beanB.getPrice());
        System.out.println("Consumption Limit: " + beanB.getConsumptionLimit());
        System.out.println("Discount: " + beanB.getDiscount());
        System.out.println("beanCList: " + beanB.getBeanCList());
    }
}
