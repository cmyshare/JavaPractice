package com.open.rule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @version 1.0
 * @Author cmy
 * @Date 2025/9/14 11:49
 * @desc Alibaba QLExpress Spring Boot 应用主类
 */

@SpringBootApplication
public class QLExpressApplication {

    public static void main(String[] args) {
        SpringApplication.run(QLExpressApplication.class, args);
        System.out.println("=================================");
        System.out.println("QLExpress Spring Boot应用启动成功!");
        System.out.println("访问地址: http://localhost:8080");
        System.out.println("API文档: http://localhost:8080/swagger-ui.html");
        System.out.println("=================================");
    }
}
