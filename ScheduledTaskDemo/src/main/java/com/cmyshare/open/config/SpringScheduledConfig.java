package com.cmyshare.open.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;

/**
 * @version 1.0
 * @Author cmy
 * @Date 2023/11/4 18:22
 * @desc Spring定时任务配置类
 */


@Configuration
@EnableAsync //开启异步任务
@EnableScheduling //开启定时任务
public class SpringScheduledConfig{

}
