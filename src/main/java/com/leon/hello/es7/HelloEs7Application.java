package com.leon.hello.es7;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@EnableCaching // 允许缓存
@ComponentScan("com.leon.hello.es7.**")
@SpringBootApplication
public class HelloEs7Application {

    public static void main(String[] args) {
        SpringApplication.run(HelloEs7Application.class, args);
    }

}
