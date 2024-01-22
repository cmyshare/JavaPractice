package com.open.javabasetool.javadoc;

import java.util.Objects;

/**
 * @version 1.0
 * @Author cmy
 * @Date 2024/1/19 14:17
 * @desc
 */
public class JavaDocTest {
    public static void main(String[] args) {
        String beanFilePath = "D:\\intellij2020-1\\idea\\JavaPractice\\JavaBaseToolDemo\\src\\main\\java\\com\\open\\javabasetool\\Student.java";
        DocVO docVO = DocUtil.execute(beanFilePath);
        if (Objects.nonNull(docVO) && Objects.nonNull(docVO.getFieldVOList())){
            docVO.getFieldVOList().forEach(System.out::println);
        }
    }
}
