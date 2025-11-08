package com.cmy.practice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @Author cmy
 * @Date 2025/11/7 11:25
 * @desc 第六步：Controller 测试
 */
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/order")
    public String createOrder(@RequestBody Map<String, Object> request) {
        String userId = (String) request.get("userId");
        List<String> itemIds = (List<String>) request.get("itemIds");
        orderService.createOrder(userId, itemIds);
        return "订单创建成功";
    }
}