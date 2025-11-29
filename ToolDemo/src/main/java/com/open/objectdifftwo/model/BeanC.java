package com.open.objectdifftwo.model;

import com.open.objectdifftwo.DiffLog;
import com.open.objectdifftwo.DiffLogKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by colinsu
 *
 * @date 2019/9/6.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BeanC {

    @DiffLog(name = "cid")
    @DiffLogKey(name = "cid")
    private Long cid ;

    @DiffLog(name = "C名称")
    private String cname;

    @DiffLog(name = "开始时间",dateFormat = "yyyy-MM-dd hh:mm:ss:SSS")
    private Date startDate;

    @DiffLog(name = "D")
    private BeanD beanD;

}
