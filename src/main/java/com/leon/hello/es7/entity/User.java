package com.leon.hello.es7.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @PROJECT_NAME: hello-es
 * @CLASS_NAME: User
 * @AUTHOR: OceanLeonAI
 * @CREATED_DATE: 2021/7/22 15:59
 * @Version 1.0
 * @DESCRIPTION:
 **/
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "user")
public class User {

    // 主键
    private String id;

    // 姓名
    private String name;

    // 性别
    private int sex;

    // 年龄
    private int age;

    // 爱好
    private String[] hobby;
}
