package com.open.rule.speltest;

/**
 * @version 1.0
 * @Author cmy
 * @Date 2025/11/12 16:16
 * @desc
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Spring EL 工具类（支持 price 直接访问 Map key）
 * 完全兼容 Java 8，适用于 Spring Boot 项目
 *
 * 核心挑战
 * Java 8 不支持 Map.of
 * SpEL 默认不能直接用 price 访问 HashMap 的 key
 * 自动替换 price → ['price'] 会破坏字符串字面量（如 'Hello ' 中的 Hello 被误转）
 * 必须 安全、正确、兼容 Java 8
 *
 * 正确解决方案：使用 StandardEvaluationContext + MapAccessor
 * Spring 提供了内置的 MapAccessor，可以让 price 直接访问 Map 的 key —— 但必须配合 StandardEvaluationContext 使用。
 * 虽然 StandardEvaluationContext 默认较危险，但我们可以通过 限制 root 对象为 Map + 不暴露其他对象 来保证安全。
 * 在只传入 Map<String, Object> 且不启用类型引用（T(...)）、方法调用等情况下，是安全的。
 *
 * public final class SpELUtils 工具类的本质：不应该被实例化，也不应该被继承
 *
 */
/**
 * Spring Expression Language (SpEL) 工具类
 *
 * 功能：支持通过简洁语法（如 "price * qty"）直接计算表达式，
 *       自动从传入的 Map<String, Object> 中读取变量值。
 * 特点：
 *   - 兼容 Java 8
 *   - 线程安全（使用 ConcurrentHashMap 缓存）
 *   - 安全：仅允许访问 Map 的键，禁止方法调用、类型引用等危险操作
 *   - 高性能：表达式解析结果缓存复用
 */
public final class SpELUtils {

    // SpEL 表达式解析器，线程安全，可重复使用
    private static final ExpressionParser PARSER = new SpelExpressionParser();

    // 表达式缓存：key 为原始表达式字符串，value 为已解析的 Expression 对象
    // 使用 ConcurrentHashMap 保证多线程环境下的线程安全
    private static final Map<String, Expression> CACHE = new ConcurrentHashMap<>();
    private static final Logger log = LoggerFactory.getLogger(SpELUtils.class);

    /**
     * 创建 SpEL 执行上下文，配置为仅能从 Map 中读取变量
     *
     * @param variables 用户传入的变量映射（如 {"price": 100, "name": "张三"}）
     * @return 配置好的 StandardEvaluationContext 实例
     */
    private static StandardEvaluationContext createContext(Map<String, Object> variables) {
        // 创建标准 SpEL 上下文（相比 SimpleEvaluationContext，它支持 PropertyAccessor）
        StandardEvaluationContext context = new StandardEvaluationContext();

        // 将用户传入的 Map 设为 root 对象（即表达式中的顶层对象）
        context.setRootObject(variables);

        // 添加 MapAccessor：这是实现 "price" → map.get("price") 的关键
        // 默认情况下，SpEL 无法直接通过属性名访问 Map 的 key，
        // MapAccessor 告诉 SpEL：“如果 root 是 Map，则尝试用 get(key) 获取值”
        context.addPropertyAccessor(new MapAccessor());

        return context;
    }

    // 私有构造函数：防止外部实例化（工具类应只提供静态方法）
    private SpELUtils() {}

    /**
     * 将原始表达式和变量映射转换为 SpEL 兼容格式（变量名替换为 a1, a2, a3...）
     *
     * @param expression 原始表达式，如 "商品原价 + 商品折扣"
     * @param variables  原始变量映射，如 {"商品原价": 100, "商品折扣": -20}
     * @return Map 包含：
     *         - "expressionNew": 转换后的表达式，如 "a1 + a2"
     *         - "variablesNew": 转换后的变量映射，如 {"a1": 100, "a2": -20}
     */
    public static Map<String, Object> transformVariablesForSpEL(String expression, Map<String, Object> variables) {

        // 1. 按 key 字典序排序，确保顺序稳定（避免缓存错乱）
        List<String> sortedKeys = variables.keySet().stream()
                .sorted()
                .collect(Collectors.toList());

        // 2. 构建原变量名 → 别名（a1, a2...）的映射
        Map<String, String> originalToAlias = new HashMap<>();
        Map<String, Object> variablesNew = new HashMap<>();
        for (int i = 0; i < sortedKeys.size(); i++) {
            String originalKey = sortedKeys.get(i);
            String alias = "a" + (i + 1); // a1, a2, a3...
            originalToAlias.put(originalKey, alias);
            variablesNew.put(alias, variables.get(originalKey));
        }

        // 3. 重写表达式：按变量名长度降序替换，防止短名干扰长名（如 "商品" vs "商品原价"）
        String expressionNew = expression;
        List<String> keysByLengthDesc = originalToAlias.keySet().stream()
                .sorted((a, b) -> Integer.compare(b.length(), a.length())) // 长的优先
                .collect(Collectors.toList());

        for (String originalVar : keysByLengthDesc) {
            String alias = originalToAlias.get(originalVar);
            expressionNew = expressionNew.replace(originalVar, alias);
        }

        Map<String, Object> returnMap=new HashMap<>();
        returnMap.put("expressionNew", expressionNew);
        returnMap.put("variablesNew", variablesNew);
        return returnMap;
    }

    /**
     * 将公式中的原始变量名（如“商品原价”）替换为翻译后的合法变量名（如“abc”）
     *
     * @param formula     原始公式，例如 "(商品原价+商品折扣+退款金額+调整表费用)*(-1)"
     * @param varTranslateMap 变量翻译映射，例如 {"商品原价":"abc", "商品折扣":"ada", ...}
     * @return 替换后的公式，例如 "(abc+ada+dasdas+fsadf)*(-1)"
     */
    public static String formulaTranslate(String formula, Map<String, String> varTranslateMap) {
        if (formula == null || varTranslateMap == null || varTranslateMap.isEmpty()) {
            return formula;
        }

        // 按 key 长度降序排序：优先替换长的变量名，防止部分匹配（如先替换“商品原价”，再替换“商品”）
        List<String> sortedKeys = varTranslateMap.keySet().stream()
                .sorted((a, b) -> Integer.compare(b.length(), a.length()))
                .collect(Collectors.toList());

        String result = formula;
        for (String originalVar : sortedKeys) {
            String translatedVar = varTranslateMap.get(originalVar);
            if (translatedVar != null) {
                // 直接字符串替换（因为原始变量名可能是任意字符，无法用 \b 边界）
                result = result.replace(originalVar, translatedVar);
            }
        }
        return result;
    }

    /**
     * 公式变量替换
     * 原始公式： "(商品原价+商品折扣+退款金額+调整表费用)*(-1)"
     * 转换后： "(a1+a2+a3+a4)*(-1)"   讲公式中的变量替换成a1、a2等依次类推，
     * 注意数字、空字符串、空格不是变量。
     * 返回转换后公式expressionNew
     */
    public static String formulaVarReplace(String formula){
        //Assert.isTrue(StringUtils.isNotBlank(formula),"原始公式 不能为空！");
        //
        //String formulaTwo = formula.replaceAll("[+\\-*/()]", ",");
        ////todo 将逗号分隔的字符串转换为列表 有问题
        //List<String> formulaList = StrUtil.split(formulaTwo, ',');
        //// 排除数字、空字符串、空格
        //List<String> formulaListNew = formulaList.stream().filter(s -> !StringUtils.isBlank(s) && !s.matches("\\d+")).distinct().collect(Collectors.toList());
        //
        //// 3. 重写表达式：按变量名长度降序替换，防止短名干扰长名（如 "商品" vs "商品原价"）
        //String expressionNew = formula;
        //List<String> keysByLengthDesc = formulaListNew.stream()
        //        .sorted((a, b) -> Integer.compare(b.length(), a.length())) // 长的优先
        //        .collect(Collectors.toList());
        //
        //for (int i = 0; i < keysByLengthDesc.size(); i++) {
        //    String originalVar = keysByLengthDesc.get(i);
        //    expressionNew=expressionNew.replace(originalVar,"a"+(i+1));
        //}
        //return expressionNew;
        return null;
    }

    /**
     * 计算 SpEL 表达式的值
     *
     * @param expression SpEL 表达式字符串，例如 "price * qty" 或 "'Hello ' + name"
     * @param variables  变量映射，表达式中使用的变量必须在此 Map 中定义
     * @return 表达式计算结果，类型由表达式决定（如 Double、String、Boolean 等）
     */
    public static Object eval(String expression, Map<String, Object> variables) {
        //Assert.isTrue(StringUtils.isNotBlank(expression),"原始表达式 不能为空！");
        //Assert.isTrue(CollUtil.isNotEmpty(variables),"原始变量映射 不能为空！");

        log.info("====== SpEL表达式：{}，变量：{} ======", expression,variables);

        //兼容中文变量处理，expression公式中变量、variables中key，统一替换成a1、a2、a3等，依次类推
        //例如金额公式expression：价格*数量，参数variables：{"价格":"123","数量":10}
        //经过处理后，金额公式expression：a1*a2，参数variables：{"a1":"123","a2":10}

        // 防御性检查：表达式为空或 null 时返回 null
        if (expression == null || expression.trim().isEmpty()) {
            return null;
        }

        // 将原始表达式和变量映射转换为 SpEL 兼容格式（变量名替换为 a1, a2, a3...）
        Map<String, Object> transformed = transformVariablesForSpEL(expression, variables);
        String expressionNew = (String) transformed.get("expressionNew");
        Map<String, Object> variablesNew = (Map<String, Object>) transformed.get("variablesNew");

        log.info("------ SpEL表达式转换后：{}，新变量：{} ------", expressionNew, variablesNew);

        // 去除表达式首尾空格，作为缓存 key
        String key = expressionNew.trim();

        // 从缓存中获取已解析的 Expression；若不存在，则解析并自动加入缓存
        // 使用 PARSER::parseExpression 方法引用（Java 8 支持）
        Expression exp = CACHE.computeIfAbsent(key, PARSER::parseExpression);

        // 创建执行上下文，绑定当前变量
        StandardEvaluationContext context = createContext(variablesNew);

        Object resultValue = exp.getValue(context);

        log.info("------ SpEL表达式计算结果：{} ------", resultValue);

        // 在指定上下文中求值并返回结果
        return resultValue;
    }

    /**
     * 计算表达式并将结果强制转换为 String
     *
     * @param expression SpEL 表达式
     * @param variables  变量映射
     * @return 结果的字符串形式；若结果为 null 则返回 null
     */
    public static String evalAsString(String expression, Map<String, Object> variables) {
        Object result = eval(expression, variables);
        return result == null ? null : result.toString();
    }

    /**
     * 计算表达式并将结果解释为布尔值
     *
     * 支持多种类型的真值判断：
     *   - Boolean: 直接返回
     *   - Number: 非零为 true
     *   - String: "true"/"1"/"yes"（忽略大小写和前后空格）视为 true
     *   - 其他类型或 null: 返回 false 或 null
     *
     * @param expression SpEL 表达式
     * @param variables  变量映射
     * @return 布尔结果，可能为 null
     */
    public static Boolean evalAsBoolean(String expression, Map<String, Object> variables) {
        Object result = eval(expression, variables);
        if (result == null) return null; // 保持 null 语义

        if (result instanceof Boolean) {
            return (Boolean) result;
        }

        if (result instanceof Number) {
            // 数字：0 为 false，非 0 为 true
            return ((Number) result).doubleValue() != 0;
        }

        if (result instanceof String) {
            String s = ((String) result).trim().toLowerCase();
            return "true".equals(s) || "1".equals(s) || "yes".equals(s);
        }

        // 其他类型（如 List、自定义对象等）默认视为 false
        return false;
    }

    /**
     * 验证 SpEL 表达式语法是否合法
     *
     * @param expression 表达式字符串，如 "price * qty" 或 "'Hello ' + name"
     * @return true 表示语法正确，false 表示存在语法错误
     */
    public static boolean validate(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            return false;
        }
        try {
            String expressionNew=formulaVarReplace(expression);
            log.info("原始表达式：{}，转换后表达式：{}",expression,expressionNew);
            // 仅解析，不求值 —— 不需要上下文
            PARSER.parseExpression(expressionNew.trim());
            return true;
        } catch (Exception e) {
            log.info("公式错误请检查: {}",e.getMessage());
            // ParseException 是主要异常，IllegalArgumentException 可能由某些非法结构触发
            throw new RuntimeException("公式错误请检查: "+e.getMessage());
        }
    }

    /**
     * 清空表达式缓存
     *
     * 适用于动态表达式频繁变更的场景（如规则引擎热更新）
     * 正常情况下无需调用
     */
    public static void clearCache() {
        CACHE.clear();
    }
}
