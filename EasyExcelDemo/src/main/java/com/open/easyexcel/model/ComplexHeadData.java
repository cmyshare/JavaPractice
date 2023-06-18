package com.open.easyexcel.model;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/4/29 0029 16:39
 * @description 复杂头写入对象
 */
@Getter
@Setter
// @EqualsAndHashCode会生成equals(Object other) 和 hashCode()方法
@EqualsAndHashCode
public class ComplexHeadData {
    @ExcelProperty({"主标题", "字符串标题"})
    private String string;
    @ExcelProperty({"主标题", "日期标题"})
    private Date date;
    @ExcelProperty({"主标题", "数字标题"})
    private Double doubleData;
}
