package com.cmy.practice.controller;

import com.cmy.practice.message.MQOrderProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

/**
 * @author cmy
 * @version 1.0
 * @date 2023/1/3 23:41
 * @description 发送消息
 */
@EnableBinding(MQOrderProcessor.class)
@RestController
public class OrderMessageProvider  {
    @Autowired
    private MQOrderProcessor processor;

    @GetMapping(value = "/sendMessage")
    public String sendCancelOrderMessage(){
        String serial = UUID.randomUUID().toString();
        //3、使用发送管道，消息绑定，构建消息返回Message实体，放入发送send中
        processor.delayOrderOutput().send(MessageBuilder.withPayload(serial).build());
        System.out.println("*****生产serial: "+serial+"*******发送时间"+new Date());
        return "success";
    }
}
