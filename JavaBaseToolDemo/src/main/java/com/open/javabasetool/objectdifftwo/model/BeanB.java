package com.open.javabasetool.objectdifftwo.model;


import com.open.javabasetool.objectdifftwo.DiffLog;
import com.open.javabasetool.objectdifftwo.DiffLogKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
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
public class BeanB {

    @DiffLogKey(name = "订单编号")
    @DiffLog(name = "主键")
    private Long id ;

    @DiffLog(name = "机场")
    private String name;

    @DiffLog(name = "开始时间",dateFormat = "yyyy-MM-dd hh:mm:ss")
    private Date startDate;

    @DiffLog(name = "订单金额", ignore = true)
    private BigDecimal price;

    @DiffLog(name = "消费限制")
    private Boolean consumptionLimit;

    @DiffLog(name = "嵌套集合C",nestedCollect = false)
    private List<BeanC> beanCList;

    private double discount;

    public BeanB(Long id, String name,Date startDate) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
    }
}
