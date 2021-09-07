package com.leon.hello.es7;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.leon.hello.es7.entity.metacat.DataMapElasticSearchDto;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @PROJECT_NAME: hello-es7
 * @CLASS_NAME: MetaTableTest
 * @AUTHOR: OceanLeonAI
 * @CREATED_DATE: 2021/9/7 13:48
 * @Version 1.0
 * @DESCRIPTION:
 **/
@Slf4j
@SpringBootTest
public class MetaTableTest {

    /**
     * 客户端
     */
    @Autowired
    private RestHighLevelClient client;

    /**
     * 通过数据源名称查询es数据
     *
     * @throws IOException
     */
    @Test
    void queryMetadataByCatalogName() throws IOException {

        SearchRequest searchRequest = new SearchRequest("meta_table");

        // 构建搜索条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        // 查询条件可以用 QueryBuilders 构建
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        // 数据源
        boolQuery.must(QueryBuilders.matchQuery("name.catalogName", "oracle_test11")); //

        // 数据库
        boolQuery.must(QueryBuilders.matchQuery("name.databaseName", "zhouchun")); //

        sourceBuilder.query(boolQuery);

        // 要返回的字段
//        sourceBuilder.fetchField("dataMapElasticSearchDto");

        /**
         * es 查询结果中包含的字段
         *
         * @see com.netflix.metacat.common.dto.TableDto
         */
        final String[] ES_RESULT_INCLUDES_FIELDS = {
                "dataMapElasticSearchDto.tableName",   // 表名称
                "dataMapElasticSearchDto.tableCnName", // 表中文名称
                "dataMapElasticSearchDto.description", // 描述
                "dataMapElasticSearchDto.createTimeString" // 创建时间
        };

        final String ES_RESULT_SORT_FIELDS = "dataMapElasticSearchDto.createTimeString";


        sourceBuilder.fetchSource(ES_RESULT_INCLUDES_FIELDS, null);

        sourceBuilder.sort(ES_RESULT_SORT_FIELDS, SortOrder.DESC);


        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        sourceBuilder.from(0); // 设置分页
        sourceBuilder.size(Integer.MAX_VALUE);

        // 将查询条件放入查询请求
        searchRequest.source(sourceBuilder);

        // 执行请求
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
//        System.out.println(searchResponse);
//        System.out.println(JSON.toJSONString(searchResponse.getHits()));
        System.out.println("==================遍历======================");
        System.out.println("查询到 " + searchResponse.getHits().getHits().length + " 条数据");
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            Object dataMapElasticSearchDto = sourceAsMap.get("dataMapElasticSearchDto");
            // System.out.println(dataMapElasticSearchDto);
            DataMapElasticSearchDto mapElasticSearchDto = JSONObject.parseObject(JSON.toJSONString(dataMapElasticSearchDto), DataMapElasticSearchDto.class);
            System.out.println("mapElasticSearchDto ---> " + mapElasticSearchDto);
            // System.out.println(hit.getSourceAsString());
        }
    }
}
