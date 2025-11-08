package com.cmy.practice;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @version 1.0
 * @Author cmy
 * @Date 2025/11/7 10:44
 * @desc 第三步：定义消息体
 */
@Data
public class OrderCreatedEvent {

    private String orderId;

    private String userId;

    private List<String> itemIds;

    private LocalDateTime createTime;
}
