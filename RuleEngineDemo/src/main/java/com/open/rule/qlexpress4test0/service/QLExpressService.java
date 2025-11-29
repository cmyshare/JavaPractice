package com.open.rule.qlexpress4test0.service;

import com.alibaba.qlexpress4.Express4Runner;
import com.alibaba.qlexpress4.QLOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author cmy
 * @Date 2025/9/14 11:54
 * @version 1.0
 * @desc QLExpress表达式执行服务
 */

@Service
public class QLExpressService {

    @Autowired
    private Express4Runner expressRunner;

    /**
     * 执行表达式
     *
     * @param expression 表达式字符串
     * @param context 上下文参数
     * @return 执行结果
     * @throws Exception 执行异常
     */
    public Object executeExpression(String expression, Map<String, Object> context) throws Exception {
        Map<String, Object> expressContext = new HashMap<>();

        // 添加上下文参数
        if (context != null) {
            for (Map.Entry<String, Object> entry : context.entrySet()) {
                expressContext.put(entry.getKey(), entry.getValue());
            }
        }

        return expressRunner.execute(expression, expressContext, QLOptions.DEFAULT_OPTIONS);
    }

    /**
     * 简单表达式执行（无参数）
     *
     * @param expression 表达式字符串
     * @return 执行结果
     * @throws Exception 执行异常
     */
    public Object executeSimpleExpression(String expression) throws Exception {
        return executeExpression(expression, null);
    }

    /**
     * 验证表达式语法
     *
     * @param expression 表达式字符串
     * @return 是否语法正确
     */
    public boolean validateExpression(String expression) {
        try {
            expressRunner.parseToSyntaxTree(expression);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 编译表达式（预编译以提高性能）
     *
     * @param expression 表达式字符串
     * @throws Exception 编译异常
     */
    public void compileExpression(String expression) throws Exception {
        expressRunner.parseToDefinitionWithCache(expression);
    }
}
