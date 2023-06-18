package com.open.easyexcel.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/6/24 0024 9:48
 * @description 基础数据类
 */
@Getter
@Setter
@EqualsAndHashCode
public class UploadData {
    private String string;
    private Date date;
    private Double doubleData;
}