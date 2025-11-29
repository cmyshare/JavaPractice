package com.cmy.practice;

import lombok.Data;

/**
 * @version 1.0
 * @Author cmy
 * @Date 2025/11/5 15:43
 * @desc
 */
@Data
public class FinancialRecord {
    private String department;
    private String item;
    private Double amount;
    private String status;
    private String remark;

    // 构造函数、getter/setter 省略
    public FinancialRecord(String department, String item, Double amount, String remark) {
        this.department = department;
        this.item = item;
        this.amount = amount;
        this.remark = remark;
    }

    // 构造 + getter/setter
    public FinancialRecord(String department, String item, Double amount, String status, String remark) {
        this.department = department;
        this.item = item;
        this.amount = amount;
        this.status = status;
        this.remark = remark;
    }
}