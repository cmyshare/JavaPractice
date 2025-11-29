package com.open.rule.qlexpress4test;

import com.alibaba.qlexpress4.Express4Runner;
import com.alibaba.qlexpress4.InitOptions;
import com.alibaba.qlexpress4.QLOptions;
import com.alibaba.qlexpress4.QLResult;

import java.util.Map;

/**
 * QLExpress 表达式工具类（Java 8 兼容）
 *
 * 特点：
 * - 支持直接使用变量名：如 "price * qty"
 * - 自动从 Map<String, Object> 注入变量
 * - 表达式解析结果缓存（提升性能）
 * - 禁用危险操作（如 System.exit、文件操作等）
 */
public final class QLExpressUtils {

    // ExpressRunner 是线程安全的，可复用
    private static final Express4Runner RUNNER = new Express4Runner(InitOptions.DEFAULT_OPTIONS);

    /**
     * 执行表达式
     *
     * @param expression 表达式字符串
     * @param context 上下文参数
     * @return 执行结果
     * @throws Exception 执行异常
     */
    public static QLResult eval(String expression, Map<String, Object> context) throws Exception {
        return RUNNER.execute(expression, context, QLOptions.DEFAULT_OPTIONS);
    }

    /**
     * 语法验证+执行表达式
     *
     * @param expression 表达式字符串
     * @param context 上下文参数
     * @return 执行结果
     * @throws Exception 执行异常
     */
    public static QLResult evalValidate(String expression, Map<String, Object> context) throws Exception {
        validate(expression);
        return eval(expression, context);
    }

    /**
     * 语法验证+缓存加速+执行表达式
     *
     * @param expression 表达式字符串
     * @param context 上下文参数
     * @return 执行结果
     * @throws Exception 执行异常
     */
    public static QLResult evalValidateCache(String expression, Map<String, Object> context) throws Exception {
        validate(expression);
        compileExpression(expression);
        return eval(expression, context);
    }

    /**
     * 简单表达式执行（无参数）
     *
     * @param expression 表达式字符串
     * @return 执行结果
     * @throws Exception 执行异常
     */
    public static QLResult evalSimple(String expression) throws Exception {
        return eval(expression, null);
    }

    /**
     * 验证表达式语法
     *
     * @param expression 表达式字符串
     * @return 是否语法正确
     */
    public static boolean validate(String expression) {
        try {
            RUNNER.parseToSyntaxTree(expression);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("验证表达式语法失败:"+e);
        }
    }

    /**
     * 编译表达式（预编译以提高性能）
     *
     * @param expression 表达式字符串
     * @throws Exception 编译异常
     */
    public static void compileExpression(String expression) throws Exception {
        RUNNER.parseToDefinitionWithCache(expression);
    }

}
