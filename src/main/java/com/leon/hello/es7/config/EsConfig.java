package com.leon.hello.es7.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class EsConfig {
    @Autowired
    private EsProperties esProperties;

    @Bean
    public RestHighLevelClient restHighLevelClient() {

        // 配置你 es 的账号密码
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(esProperties.getUsername(), esProperties.getPassword()));

        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(esProperties.getHost(), esProperties.getPort(), "http")).setHttpClientConfigCallback(httpAsyncClientBuilder -> {
                    httpAsyncClientBuilder.disableAuthCaching();
                    return httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                }));
        return client;
    }

}
