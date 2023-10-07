package com.open.mongodb.template;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @version 1.0
 * @Author cmy
 * @Date 2023/10/7 9:17
 * @desc mongodb对象User
 */
@Data
@Document("User") //使用@Document("User")定义User对象
public class User {
    @Id
    private String id;
    private String name;
    private Integer age;
    private String email;
    private String createDate;
}

