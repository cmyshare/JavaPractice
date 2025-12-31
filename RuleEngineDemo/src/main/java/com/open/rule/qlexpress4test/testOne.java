package com.open.rule.qlexpress4test;

import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0
 * @Author cmy
 * @Date 2025/11/12 17:04
 * @desc
 */
public class testOne {
    public static void main(String[] args) {
        Map<String, Object> vars = new HashMap<>();
        vars.put("price", 99.9);
        vars.put("qty", 3);
        vars.put("pr ice", 99.9);
        vars.put("q ty", 3);
        vars.put("Hello", "你好");
        vars.put("name", "张三");
        vars.put("isVip", true);
        vars.put("价格", 10.222222);
        vars.put("数量", 100);

        Object o=299.7;
        Object o2="你好张三";
        Object o3=true;
        System.out.println(o.equals(299.7));
        System.out.println(o2.equals("你好张三"));
        System.out.println(o3.equals(true));

        try {
            //System.out.println(QLExpressUtils.evalValidate("price *** qty", vars).getResult());

            //System.out.println(QLExpressUtils.eval("pr ice * q ty", vars).getResult());
            System.out.println(QLExpressUtils.eval("价格 * 数量", vars).getResult());

            //System.out.println(QLExpressUtils.eval("price * qty", vars).getResult());                    // 299.7
            //System.out.println(QLExpressUtils.eval("'Hello ' + name", vars).getResult());       // Hello 张三
            //System.out.println(QLExpressUtils.eval("Hello + name", vars).getResult());       // 你好张三
            //System.out.println(QLExpressUtils.eval("isVip && qty > 0", vars).getResult());     // true
            //System.out.println(QLExpressUtils.eval("price > 50 ? '贵' : '便宜'", vars).getResult());   // 贵
            //Object result = QLExpressUtils.eval("true && false", vars).getResult();
            //System.out.println(result);                 // false
            //if (result instanceof Boolean){
            //    Boolean bool = (Boolean) result;
            //    System.out.println(bool);                 // false
            //}

            System.out.println(QLExpressUtils.validate("(商品原价(得瑟得瑟)+商品折扣+退款金額+调整表费用)*(-1)"));
            System.out.println(QLExpressUtils.provideVariableTwo("(商品原价(得瑟得瑟)+商品折扣+退款金額+调整表费用)*(-1)"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
