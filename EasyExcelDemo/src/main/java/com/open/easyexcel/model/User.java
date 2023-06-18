package com.open.easyexcel.model;

import lombok.Data;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/5/16 0016 13:41
 * @description 用户表
 */

@Data
public class User {
    private Long id;
    private Long companyId;
    private Long theCompanyId;
    private String userNum;
    private String userName;
    private String telephone;
    private String addressDetail;
    private String areaLevelNameStr;
}
