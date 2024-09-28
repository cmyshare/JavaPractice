package com.open.javabasetool.objectcopy;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.convert.Convert;
import lombok.Data;

import java.util.Date;

/**
 * @version 1.0
 * @Author cmy
 * @Date 2024/9/23 14:17
 * @desc 对象复制工具测试
 */
public class BeanUtilTest {
    public static void main(String[] args) {
        // 假设 m 是你的源对象
        SourceObject m = new SourceObject();
        m.setSku_id("CU-6");
        m.setName("安德玛男鞋");
        m.setSale_price(259.00);
        m.setCost_price(101.00);
        m.setModified(new Date());

        // 目标对象
        GlobalJstItemSkusBo globalJstItemSkusBo = BeanUtil.toBean(m, GlobalJstItemSkusBo.class, CopyOptions.create().setIgnoreCase(true));

        // 输出结果
        System.out.println(globalJstItemSkusBo.getSkuId());
        System.out.println(globalJstItemSkusBo.getName());
        System.out.println(globalJstItemSkusBo.getSalePrice());
        System.out.println(globalJstItemSkusBo.getCostPrice());
        System.out.println(globalJstItemSkusBo.getModified());
    }
}

// 源对象
@Data
class SourceObject {
    private String sku_id;
    private String name;
    private double sale_price;
    private double cost_price;
    private Date modified;
}

// 目标对象
@Data
class GlobalJstItemSkusBo {
    private String skuId;
    private String name;
    private Double salePrice;
    private Double costPrice;
    private Date modified;
}
