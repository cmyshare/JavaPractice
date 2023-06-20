package com.open.mysql;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @version 1.0
 * @Author cmy
 * @Date 2023/6/20 11:20
 * @desc
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@MapperScan(value = "com.open.mysql.mapper")
public class MysqlDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(MysqlDemoApplication.class, args);
    }
}
