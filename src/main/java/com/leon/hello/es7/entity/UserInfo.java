package com.leon.hello.es7.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @PROJECT_NAME: hello-es7
 * @CLASS_NAME: UserInfo
 * @AUTHOR: OceanLeonAI
 * @CREATED_DATE: 2021/8/19 16:41
 * @Version 1.0
 * @DESCRIPTION:
 **/
@Data
@ToString
public class UserInfo {
    private Integer id;
    private String name;
    private String sex;
    private Integer age;
}
