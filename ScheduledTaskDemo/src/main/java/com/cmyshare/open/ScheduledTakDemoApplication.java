package com.cmyshare.open;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @version 1.0
 * @Author cmy
 * @Date 2023/11/4 18:17
 * @desc SpringBoot集成定时任务启动类
 */
@SpringBootApplication
public class ScheduledTakDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(ScheduledTakDemoApplication.class, args);
    }
}
