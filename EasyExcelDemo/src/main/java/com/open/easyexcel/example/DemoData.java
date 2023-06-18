package com.open.easyexcel.example;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/4/28 0028 16:08
 * @description Demo对象类
 */
@Data
public class DemoData {
    //index从0开始，逐个设置excel表头名称
    @ExcelProperty(value = "老师编号",index = 0)
    private Integer sno;
    @ExcelProperty(value = "老师姓名",index = 1)
    private String sname;
}