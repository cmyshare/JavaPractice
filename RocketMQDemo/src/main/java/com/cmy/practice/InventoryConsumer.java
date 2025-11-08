package com.cmy.practice;

import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @Author cmy
 * @Date 2025/11/7 11:24
 * @desc 第五步：消费者（库存服务）
 */
@Service
@RocketMQMessageListener(
        topic = "ORDER_TOPIC",
        consumerGroup = "inventory-consumer-group",
        consumeMode = ConsumeMode.CONCURRENTLY  // 并发消费
)
public class InventoryConsumer implements RocketMQListener<OrderCreatedEvent> {

    @Override
    public void onMessage(OrderCreatedEvent event) {
        System.out.println("收到订单消息，开始扣减库存: " + event.getOrderId());
        // TODO: 扣减库存逻辑
        // 若失败可抛异常触发重试（默认重试16次）
    }
}
