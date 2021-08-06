package com.leon.hello.es7;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Map;

@Slf4j
@SpringBootTest
class HelloEs7ApplicationTests {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Test
    void testEs7() {
        String index = ".security-7";
        String id = "reserved-user-apm_system";
        GetRequest getRequest = new GetRequest(index).id(id);
        try {
            GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
            Map<String, Object> data = getResponse.getSource();
            log.info("查询到索引{}下id为{}的数据为 ---> {}", index, id, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testEs7Delete() {
        String index = "index";
        String id = "ESa3GnsBy1yhr2YKor9U";
        DeleteRequest deleteRequest = new DeleteRequest(index, id);
        try {
            DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
            System.out.println(deleteResponse.status());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
