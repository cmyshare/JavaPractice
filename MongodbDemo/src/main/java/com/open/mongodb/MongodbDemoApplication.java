package com.open.mongodb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @version 1.0
 * @Author cmy
 * @Date 2023/6/20 11:20
 * @desc SpringBoot集成Mongodb
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MongodbDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(MongodbDemoApplication.class, args);
    }
}
