package com.open.easyexcel.example;

import com.alibaba.excel.EasyExcel;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/4/28 0028 16:14
 * @description 读操作
 */
public class TestEasyExcelRead {
    public static void main(String[] args) {
        //实现excel读操作
        String filename = "F:\\IDEA2022\\workmenu\\easyExcelDemo\\src\\main\\java\\com\\easyexcel\\data\\test.xlsx";
        //write方法两个参数：第一个参数文件路径名称，第二个参数实体类class，第三个参数监听器、sheet为表名、doRead为读取数据函数
        //读取excel需要先写一个监听器继承AnalysisEventListener
        EasyExcel.read(filename, DemoData.class,new ExcelListener()).sheet("老师列表").doRead();
    }
}
