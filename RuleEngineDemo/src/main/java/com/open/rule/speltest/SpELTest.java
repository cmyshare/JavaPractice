package com.open.rule.speltest;

import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0
 * @Author cmy
 * @Date 2025/11/12 16:05
 * @desc Spring EL测试类
 */
public class SpELTest {
    public static void main(String[] args) {
        Map<String, Object> vars = new HashMap<>();
        vars.put("price", 99.9);
        vars.put("qty", 3);
        vars.put("Hello", "你好");
        vars.put("name", "张三");
        vars.put("isVip", true);

        System.out.println(10/3);

        //在表达式中，单引号的数据不参与计算
        System.out.println(SpELUtils.eval("price * qty", vars));                    // 299.7
        System.out.println(SpELUtils.evalAsString("'Hello' + name", vars));       // Hello 张三
        System.out.println(SpELUtils.evalAsString("Hello + name", vars));       // 你好张三
        System.out.println(SpELUtils.evalAsBoolean("isVip && qty > 0", vars));     // true
        System.out.println(SpELUtils.eval("price > 50 ? '贵' : '便宜'", vars));   // 贵
        System.out.println(SpELUtils.eval("true && false", vars));                 // false（关键字未被转换）
    }
}
