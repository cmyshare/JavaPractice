package com.open.javabasetool.objectdifftwo.model;


import com.open.javabasetool.objectdifftwo.DiffLog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Created by colinsu
 *
 * @date 2019/9/6.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BeanA {


    @DiffLog(name = "字符串a")
    private String a;

    @DiffLog(name = "字符串b", ignore = true)
    private String b;

    @DiffLog(name = "集合B")
    private List<BeanB> bList;

    @DiffLog(name = "开始时间",dateFormat = "yyyy-MM-dd hh:mm:ss")
    private Date start;

    @DiffLog(name = "价格")
    private BigDecimal price;

    @DiffLog(name = "比特")
    private Byte bit;

    @DiffLog(name = "是否")
    private Boolean aBoolean;

    @DiffLog(name = "时间")
    private LocalDateTime localDateTime;

    @DiffLog(name = "单位")
    private Short unit;

    @DiffLog(name = "对象C")
    private BeanC beanC;

    @DiffLog(name = "对象E")
    private BeanE beanE;

    @DiffLog(name = "平台",dictEnum =PlatType.class)
    private Integer plat;


    public BeanA(String a, String b, List<BeanB> bList) {
        this.a = a;
        this.b = b;
        this.bList = bList;
    }

}
