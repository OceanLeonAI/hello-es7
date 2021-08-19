package com.leon.hello.es7.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @PROJECT_NAME: hello-es7
 * @CLASS_NAME: CacheConfig
 * @AUTHOR: OceanLeonAI
 * @CREATED_DATE: 2021/8/19 16:40
 * @Version 1.0
 * @DESCRIPTION:
 **/
@Configuration
public class CacheConfig {
    @Bean
    public Cache<String, Object> caffeineCache() {
        return Caffeine.newBuilder()
                // 设置最后一次写入或访问后经过固定时间过期
//                .expireAfterWrite(60, TimeUnit.SECONDS)
                .expireAfterWrite(20, TimeUnit.SECONDS)
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(1000)
                .build();
    }
}
