package com.cmy.practice.controller;

import com.cmy.practice.message.MQOrderProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author cmy
 * @version 1.0
 * @date 2023/1/3 23:46
 * @description 接收消息
 */

@EnableBinding(MQOrderProcessor.class)
@Component
public class OrderMessageConsumer {
    @Value("${server.port}")
    private String serverPort;


    @StreamListener(MQOrderProcessor.DELAYINPUT)
    public void input(Message<String> message) {
        System.out.println("消费者,----->接受到的消息: " + message.getPayload() + "*******消费时间" + new Date() + "\t  port: " + serverPort);
    }
}
