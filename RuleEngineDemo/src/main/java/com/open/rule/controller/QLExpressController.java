package com.open.rule.controller;

/**
 * @Author cmy
 * @Date 2025/9/14 11:54
 * @version 1.0
 * @desc QLExpress表达式执行控制器
 */

import com.open.rule.service.QLExpressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/qlexpress")
public class QLExpressController {

    @Autowired
    private QLExpressService qlExpressService;

    /**
     * 首页欢迎信息
     */
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> welcome() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "欢迎使用Alibaba QLExpress Spring Boot Demo!");
        response.put("description", "这是一个集成了阿里巴巴QLExpress表达式引擎的Spring Boot应用");
        response.put("apis", new String[]{
                "POST /api/qlexpress/execute - 执行表达式",
                "POST /api/qlexpress/validate - 验证表达式语法",
                "GET /api/qlexpress/examples - 查看示例"
        });
        return ResponseEntity.ok(response);
    }

    /**
     * 执行表达式
     *
     * @param request 请求参数
     * @return 执行结果
     */
    @PostMapping("/execute")
    public ResponseEntity<Map<String, Object>> executeExpression(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            String expression = (String) request.get("expression");
            if (expression == null || expression.trim().isEmpty()) {
                response.put("success", false);
                response.put("error", "表达式不能为空");
                return ResponseEntity.badRequest().body(response);
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> context = (Map<String, Object>) request.get("context");

            //编译表达式（预编译以提高性能）
            boolean b = qlExpressService.validateExpression(expression);
            System.out.println("编译表达式（预编译以提高性能）结果："+b);

            Object result = qlExpressService.executeExpression(expression, context);

            response.put("success", true);
            response.put("expression", expression);
            response.put("result", result);
            response.put("resultType", result != null ? result.getClass().getSimpleName() : "null");

        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    /**
     * 验证表达式语法
     *
     * @param request 请求参数
     * @return 验证结果
     */
    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateExpression(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();

        String expression = (String) request.get("expression");
        if (expression == null || expression.trim().isEmpty()) {
            response.put("valid", false);
            response.put("error", "表达式不能为空");
            return ResponseEntity.badRequest().body(response);
        }

        boolean isValid = qlExpressService.validateExpression(expression);
        response.put("valid", isValid);
        response.put("expression", expression);

        if (!isValid) {
            response.put("message", "表达式语法不正确");
        }

        return ResponseEntity.ok(response);
    }

    /**
     * 获取使用示例
     *
     * @return 示例列表
     */
    @GetMapping("/examples")
    public ResponseEntity<Map<String, Object>> getExamples() {
        Map<String, Object> response = new HashMap<>();
        response.put("examples", new Object[]{
                new HashMap<String, Object>() {{
                    put("description", "简单数学计算");
                    put("expression", "1 + 2 * 3");
                    put("context", null);
                    put("expectedResult", 7);
                }},
                new HashMap<String, Object>() {{
                    put("description", "使用变量计算");
                    put("expression", "a + b * c");
                    put("context", new HashMap<String, Object>() {{
                        put("a", 10);
                        put("b", 5);
                        put("c", 2);
                    }});
                    put("expectedResult", 20);
                }},
                new HashMap<String, Object>() {{
                    put("description", "条件判断");
                    put("expression", "if(score >= 60) then '及格' else '不及格'");
                    put("context", new HashMap<String, Object>() {{
                        put("score", 85);
                    }});
                    put("expectedResult", "及格");
                }},
                new HashMap<String, Object>() {{
                    put("description", "字符串操作");
                    put("expression", "name + '的年龄是' + age + '岁'");
                    put("context", new HashMap<String, Object>() {{
                        put("name", "张三");
                        put("age", 25);
                    }});
                    put("expectedResult", "张三的年龄是25岁");
                }}
        });

        return ResponseEntity.ok(response);
    }
}
