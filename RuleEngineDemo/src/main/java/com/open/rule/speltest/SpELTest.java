package com.open.rule.speltest;

import com.open.rule.qlexpress4test.QLExpressUtils;

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
        long startTime = System.currentTimeMillis();
        System.out.println("开始时间"+startTime);

        Map<String, Object> vars = new HashMap<>();
        vars.put("price", 99.9);
        vars.put("qty", 3);
        vars.put("Coin Cashback Sponsored by Seller", 99.9);
        vars.put("Hot Listing", 3);
        vars.put("Hello", "你好");
        vars.put("name", "张三");
        vars.put("isVip", true);
        vars.put("a", 1);
        vars.put("b", 2);
        vars.put("c", 3);
        vars.put("d", 4);
        vars.put("e", 5);
        vars.put("f", 6);
        vars.put("-1",2);
        vars.put("一",2);
        vars.put("二",2);

        System.out.println(10/3);

        //在表达式中，单引号的数据不参与计算
        System.out.println(SpELUtils.eval("Coin Cashback Sponsored by Seller * Hot Listing", vars));
        System.out.println(SpELUtils.eval("一 == 二", vars));
        //System.out.println(SpELUtils.eval("price * qty", vars));                    // 299.7
        //System.out.println(SpELUtils.evalAsString("'Hello' + name", vars));       // Hello 张三
        //System.out.println(SpELUtils.evalAsString("Hello + name", vars));       // 你好张三
        //System.out.println(SpELUtils.evalAsBoolean("isVip && qty > 0", vars));     // true
        //System.out.println(SpELUtils.eval("price > 50 ? '贵' : '便宜'", vars));   // 贵
        //System.out.println(SpELUtils.eval("true && false", vars));                 // false（关键字未被转换）
        //System.out.println(SpELUtils.validate("price + "));              // false
        // 不会！在 Spring Expression Language（SpEL）中，-1 作为表达式中的字面量（literal），不会被当作变量名去 vars 中查找。
        System.out.println(SpELUtils.eval("((a+b)*c/d+(e-f))*(-1)",vars));

        System.out.println("整体耗时"+(System.currentTimeMillis()-startTime));
    }
}
