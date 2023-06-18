package com.open.easyexcel.model;

import lombok.Data;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/5/16 0016 13:52
 * @description 用户号表
 */
@Data
public class UserNumber {
    private Long id;
    private Long companyId;
    private Long theCompanyId;
    private String userNum;
    private String villageName;
    private String addressDetail;

}
