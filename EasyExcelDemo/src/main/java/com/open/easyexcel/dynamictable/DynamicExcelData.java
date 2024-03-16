package com.open.easyexcel.dynamictable;

import lombok.Data;

/**
 * @author cmy
 * @version 1.0
 * @date 2024/3/16 23:40
 * @description
 */

@Data
public class DynamicExcelData {
    //列名
    private String name;
    //默认值
    private String defaultValue;

    public DynamicExcelData(String name, String defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }
}
