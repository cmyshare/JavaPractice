package com.cmy.practice.message;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

/**
 * @author cmy
 * @version 1.0
 * @date 2023/1/3 23:39
 * @description Processor来映射上边配置的bindings
 */

@Component
public interface MQOrderProcessor {
    String INPUT="order-input";
    @Input(MQOrderProcessor.INPUT)
    SubscribableChannel orderInput();

    String DELAYINPUT="delay-input";
    @Input(MQOrderProcessor.DELAYINPUT)
    SubscribableChannel delayOrderInput();

    String OUTPUT="order-output";
    @Output(MQOrderProcessor.OUTPUT)
    MessageChannel orderOutput();

    String DELAYOUTPUT="delay-output";
    @Output(MQOrderProcessor.DELAYOUTPUT)
    MessageChannel delayOrderOutput();
}

