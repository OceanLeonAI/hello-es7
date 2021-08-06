package com.leon.hello.es7.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @PROJECT_NAME: hello-es7
 * @CLASS_NAME: EsProperties
 * @AUTHOR: OceanLeonAI
 * @CREATED_DATE: 2021/8/6 15:47
 * @Version 1.0
 * @DESCRIPTION:
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "es")
public class EsProperties {

    private String sslFolder;

    private String username;

    private String password;

    private String host;

    private Integer port;
}
