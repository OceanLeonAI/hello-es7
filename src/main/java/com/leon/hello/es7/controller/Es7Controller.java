package com.leon.hello.es7.controller;

import com.leon.hello.es7.entity.User;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

/**
 * @PROJECT_NAME: hello-es7
 * @CLASS_NAME: Es7Controller
 * @AUTHOR: OceanLeonAI
 * @CREATED_DATE: 2021/8/6 14:33
 * @Version 1.0
 * @DESCRIPTION:
 **/
@RestController
public class Es7Controller {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private User user;

    /**
     * 测试 es7
     */
    @RequestMapping("/testEs7")
    public void testEs7() {
        GetRequest getRequest = new GetRequest(".security-7").id("reserved-user-apm_system");

        try {
            GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
            Map<String, Object> map = getResponse.getSource();
            System.out.println(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
