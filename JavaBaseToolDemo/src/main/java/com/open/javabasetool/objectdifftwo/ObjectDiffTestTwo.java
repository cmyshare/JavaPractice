package com.open.javabasetool.objectdifftwo;

import com.alibaba.fastjson2.JSON;
import com.open.javabasetool.objectdifftwo.model.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @version 1.0
 * @Author cmy
 * @Date 2024/2/3 15:30
 * @desc 对象对比测试类
 */

@Data
public class ObjectDiffTestTwo {
    public static void main(String[] args) throws Exception{

        test();
    }

    private static void test() throws Exception{

        BeanB a1b = new BeanB(1L,"北京",new Date());
        a1b.setConsumptionLimit(false);
        BeanB a1b3 = new BeanB(3L,"3",new Date());
        BeanB a1b2 = new BeanB(2L, "1", new Date(), new BigDecimal(100), 0.9);
        a1b2.setConsumptionLimit(true);

        BeanC b1c = new BeanC(12L,"beanC",new Date(),new BeanD("源beand"));
        BeanC b2c = new BeanC(13L,"beanC2",new Date(),new BeanD("现beand"));

        ArrayList<BeanB> list = new ArrayList<>();
        list.add(a1b);
        list.add(a1b3);
        list.add(a1b2);

        BeanE a1e = new BeanE(1L,"e1");

        BeanA a1 = new BeanA("1","1",list);
        a1.setStart(new Date());
        a1.setBit(new Byte("11"));
        a1.setUnit(new Short("66"));
        a1.setBeanC(b1c);
        a1.setBeanE(a1e);
        a1.setaBoolean(false);
        a1.setLocalDateTime(LocalDateTime.now());

        //        a1.setPrice(new BigDecimal("10.23"));


        BeanB a2b = new BeanB(1L,"上海",new Date());
        BeanB a2b2 = new BeanB(2L,"2",new Date());

        ArrayList<BeanB> list2 = new ArrayList<>();
        list2.add(a2b);
        list2.add(a2b2);

        BeanE a2e = new BeanE(2L,"e2");

        final BeanA a2 = new BeanA("2","2",null);
        a2.setPrice(new BigDecimal("50.852236"));
        a2.setBit(new Byte("22"));
        a2.setUnit(new Short("99"));
        a2.setBeanC(b2c);
        a2.setBeanE(a2e);
        a2.setaBoolean(true);
        a2.setLocalDateTime(LocalDateTime.now().plusSeconds(1000));


        List<DiffWapper> diffWappers = AbstractObjectDiff.generateDiff(a1, a2);
        System.out.println("英文用户操作日志："+ JSON.toJSON(diffWappers));

        String s = ChineseObjectDiff.genChineseDiffStr(a1, a2);

        System.out.println("中文用户操作日志："+s);
    }
}
