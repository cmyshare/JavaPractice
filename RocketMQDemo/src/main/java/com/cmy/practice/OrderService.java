package com.cmy.practice;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @version 1.0
 * @Author cmy
 * @Date 2025/11/7 10:44
 * @desc 第四步：生产者（订单服务）
 */
@Service
public class OrderService {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public void createOrder(String userId, List<String> itemIds) {
        String orderId = "ORDER_" + System.currentTimeMillis();
        OrderCreatedEvent event = new OrderCreatedEvent();
        event.setOrderId(orderId);
        event.setUserId(userId);
        event.setItemIds(itemIds);
        event.setCreateTime(LocalDateTime.now());

        // 同步发送（也可用 asyncSend）
        SendResult result = rocketMQTemplate.syncSend("ORDER_TOPIC", MessageBuilder.withPayload(event).build());
        System.out.println("订单消息发送成功: " + result.getMsgId());
    }
}
