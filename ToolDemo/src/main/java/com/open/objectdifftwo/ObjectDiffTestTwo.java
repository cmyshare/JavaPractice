package com.open.objectdifftwo;

import com.alibaba.fastjson.JSON;
import com.open.objectdifftwo.model.BeanA;
import com.open.objectdifftwo.model.BeanB;
import com.open.objectdifftwo.model.BeanC;
import com.open.objectdifftwo.model.BeanE;
import lombok.Data;

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
    public static void main(String[] args) throws Exception {
        test2();
    }

    private static void test2() throws Exception{
        //对象1
        List<BeanC> listC = new ArrayList<>();
        BeanC beanC = new BeanC();
        beanC.setBeanD(null);
        beanC.setCid(1L);
        beanC.setCname("C");
        beanC.setStartDate(null);
        listC.add(beanC);

        BeanB a1b = new BeanB(1L,"1",new Date());
        a1b.setBeanCList(listC);
        BeanB a1b2 = new BeanB(2L,"21",new Date());
        a1b2.setBeanCList(listC);
        BeanB a1b4 = new BeanB(4L,"22",new Date());
        a1b4.setBeanCList(listC);
        BeanB a1b3 = new BeanB(3L, "3", new Date());
        a1b3.setBeanCList(listC);
        ArrayList<BeanB> listB = new ArrayList<>();
        listB.add(a1b);
        listB.add(a1b2);
        listB.add(a1b3);
        listB.add(a1b4);

        BeanE a1e = new BeanE();
        a1e.setId(1L);
        a1e.setName("e1");

        BeanA a1 = new BeanA("1","1",listB);
        a1.setBeanE(a1e);

        //对象2
        List<BeanC> listC2 = new ArrayList<>();
        BeanC beanC2 = new BeanC();
        beanC2.setBeanD(null);
        beanC2.setCid(1L);
        beanC2.setCname("C");
        beanC2.setStartDate(null);
        listC2.add(beanC2);
        BeanC beanC3 = new BeanC();
        beanC3.setBeanD(null);
        beanC3.setCid(3L);
        beanC3.setCname("3C");
        beanC3.setStartDate(null);
        listC2.add(beanC3);

        BeanB a1b22 = new BeanB(1L,"1",new Date());
        a1b22.setBeanCList(listC2);
        BeanB a1b33 = new BeanB(2L,"21",new Date());
        a1b33.setBeanCList(listC2);
        BeanB a1b44 = new BeanB(4L,"22",new Date());
        a1b44.setBeanCList(listC2);
        BeanB a1b55 = new BeanB(3L, "3", new Date());
        a1b55.setBeanCList(listC2);
        ArrayList<BeanB> listB2 = new ArrayList<>();
        listB2.add(a1b22);
        listB2.add(a1b33);
        //listB2.add(a1b44);
        //listB2.add(a1b55);
        BeanE a1e2 = new BeanE();
        a1e2.setId(2L);
        a1e2.setName("e2");

        BeanA a2 = new BeanA("2","2",listB2);
        a2.setBeanE(a1e);

        List<DiffWrappers> diffWrappers = BaseObjectDiff.generateDiff(a1, a2);
        System.out.println("英文用户操作日志："+ JSON.toJSON(diffWrappers));

        String s = ChineseObjectDiff.genChineseDiffStr(a1, a2);
        System.out.println("中文用户操作日志："+s);

        System.out.println("================================================================");

        //BeanB a1b11B = new BeanB(1L,"B1",new Date());
        //a1b11B.setBeanCList(listC);
        //BeanB a1b22B = new BeanB(2L,"B2",new Date());
        //a1b22B.setBeanCList(listC2);
        //String s2 = ChineseObjectDiff.genChineseDiffStr(a1b11B, a1b22B);
        //System.out.println("BeanB中文用户操作日志："+s2);
    }
}
