package com.leon.hello.es7;

import com.alibaba.fastjson.JSON;
import com.leon.hello.es7.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest
class HelloEs7ApplicationTests {

    @Autowired
    private RestHighLevelClient client;

    /**
     * 索引名称
     */
    private static final String INDEX = "user";

    /**
     * 用户名 15 个
     */
    private static final String[] userNames = {
            "张三", "李四", "王五", "赵六", "高七",
            "leon", "jerry", "tom", "mike", "allen",
            "赵", "钱", "孙", "李", "周"
    };

    /**
     * 性别
     */
    private static final int[] userSex = {
            0, 1, 2
    };

    /**
     * 爱好 15 个
     */
    private static final String[] userHobbies = {
            "足球", "篮球", "羽毛球", "排球", "乒乓球",
            "basketball", "football", "badminton", "volleyball", "pingpong",
            "吃", "喝", "玩", "乐", "游"
    };

    /**
     * 随机对象
     */
    private static final Random RANDOM = new Random();

    /**
     * 最大年龄数
     */
    private static final int MAX_AGE = 100;

    /**
     * 生成 32 位 UUID
     *
     * @return
     */
    private String generateUuid32() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 测试创建索引
     *
     * @throws IOException
     */
    @Test
    void testCreateIndex() throws IOException {
        //1.创建索引请求
        CreateIndexRequest request = new CreateIndexRequest(INDEX);
        //2.客户端执行请求 CreateIndexRequest，返回响应
        CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
        System.out.println(createIndexResponse);
    }

    /**
     * 测试索引是否存在
     *
     * @throws IOException
     */
    @Test
    void testExistIndex() throws IOException {
        GetIndexRequest getIndexRequest = new GetIndexRequest(INDEX);
        boolean exists = client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    /**
     * 测试删除索引
     *
     * @throws IOException
     */
    @Test
    void testDeleteIndex() throws IOException {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(INDEX);
        AcknowledgedResponse acknowledgedResponse = client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        System.out.println(acknowledgedResponse.toString());
        System.out.println(acknowledgedResponse.isAcknowledged());
    }

    /**
     * 测试判断指定索引是否存在，如果不存在则创建
     *
     * @throws IOException
     */
    @Test
    void testCreateIndexIfNotExist() throws IOException {
        GetIndexRequest getIndexRequest = new GetIndexRequest(INDEX);
        boolean exists = client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        if (!exists) {
            System.out.printf("user 不存在，执行创建操作。。。");
            //1.创建索引请求
            CreateIndexRequest request = new CreateIndexRequest(INDEX);
            //2.客户端执行请求 CreateIndexRequest，返回响应
            CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
            System.out.println(createIndexResponse);
        }
    }

    /**
     * 添加文档
     */
    @Test
    void testAddDocument() throws IOException {
        // 创建新用户
        User.UserBuilder userBuilder = User.builder();
        userBuilder
                .id(generateUuid32())
                .name("张三")
                .sex(0)
                .age(22)
                .hobby(new String[]{"篮球", "足球"});


        // 创建请求
        IndexRequest request = new IndexRequest(INDEX);

        // 规则 put/index_leon/_doc/1
        request.id("1"); // 指定文档 id
        request.timeout("1s"); // 设置超时
        // request.timeout(TimeValue.timeValueMillis(10));

        // 将对象放入请求 json
        request.source(JSON.toJSONString(userBuilder.build()), XContentType.JSON);
        System.out.println(request.sourceAsMap());
        // 发送请求，获取相应结果
        IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
        System.out.println(indexResponse.toString());
        System.out.println(indexResponse.status()); // 对应命令返回的状态 CREATED
    }

    /**
     * 判断文档是否存在
     * get/index/doc/1
     */
    @Test
    void testIsDocumentExist() throws IOException {
        GetRequest getRequest = new GetRequest(INDEX, "1");
        // 不获取返回的 _source 的上下文
        getRequest.fetchSourceContext(new FetchSourceContext(false));
        getRequest.storedFields("_none_");

        boolean exists = client.exists(getRequest, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    /**
     * 获取文档信息
     * get/index/doc/1
     */
    @Test
    void testGetDocument() throws IOException {
        GetRequest getRequest = new GetRequest(INDEX, "1");
        // 不获取返回的 _source 的上下文
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        System.out.println(getResponse.getSourceAsString()); // 打印文档的内容
        System.out.println(getResponse);
    }

    /**
     * 更新文档信息
     * get/index/doc/1
     */
    @Test
    void testUpdateDocument() throws IOException {
        UpdateRequest updateRequest = new UpdateRequest(INDEX, "1");
        updateRequest.timeout("1s");
//        User user = new User("leon", 18);
        User user = new User();
        updateRequest.doc(JSON.toJSONString(user), XContentType.JSON);
        UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
        System.out.println(updateResponse.status());
    }

    /**
     * 删除文档信息
     * get/index/doc/1
     */
    @Test
    void testDeleteDocument() throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(INDEX, "1");
        deleteRequest.timeout("1s");
        DeleteResponse deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
        System.out.println(deleteResponse);
        System.out.println(deleteResponse.status());
        System.out.println(deleteResponse.status().getStatus() == 200);
    }

    /**
     * 批量插入文档信息
     * get/index/doc/1
     */
    @Test
    void testBulkAddDocument() throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("10s");

        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            userList.add(
                    User.builder()
//                            .id(generateUuid32())
                            .id(i % 2 == 0 ? "偶数" + generateUuid32() : "奇数" + generateUuid32())
                            .name(userNames[i % userNames.length] + RANDOM.nextInt(i + 1) + userNames[i % 15]) // 随机取名字
                            .sex(userSex[i % userSex.length]) // 随机性别
                            .age(RANDOM.nextInt(MAX_AGE)) // 随机年龄
                            .hobby(new String[]{userHobbies[i % 15]}) // 随机爱好
                            .build()
            );
        }

        // 批量生成请求
        for (int i = 0; i < userList.size(); i++) {
            // 批量操作都在这儿
            bulkRequest.add(new IndexRequest(INDEX)
                    .id("" + (i + 1))
                    .source(JSON.toJSONString(userList.get(i)), XContentType.JSON));
        }

        BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println("批量保存结果 ---> " + bulkResponse.status());
        System.out.println("批量保存是否有失败 ---> " + bulkResponse.hasFailures());
    }

    /**
     * 查询文档信息
     * SearchRequest 搜索请求
     * SearchSourceBuilder 条件构造
     * HighlightBuilder 高亮构造
     * TermQueryBuilder 精确查询
     * xxxQueryBuilder 对应命令行的查询
     */
    @Test
    void testSearchDocument() throws IOException {
        SearchRequest searchRequest = new SearchRequest(INDEX);
        // 构建搜索条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // 查询条件可以用 QueryBuilders 构建

        // QueryBuilders.termQuery 精确查询
        // QueryBuilders.matchAllQuery 匹配所有
        // BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        MatchAllQueryBuilder matchAllQuery = QueryBuilders.matchAllQuery();
        sourceBuilder.query(matchAllQuery);
        // TermQueryBuilder termQuery = QueryBuilders.termQuery("name", "leon");
        // sourceBuilder.query(termQuery);

        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        sourceBuilder.from(0); // 设置分页
        sourceBuilder.size(6);

        // 将查询条件放入查询请求
        searchRequest.source(sourceBuilder);

        // 执行请求
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(searchResponse);
        System.out.println(JSON.toJSONString(searchResponse.getHits()));
        System.out.println("==================遍历======================");
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
    }


    /**
     * 批量插入文档信息
     * get/index/doc/1
     */
    @Test
    void testBulkAddDocumentForFuzzyQuery() throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("10s");

        List<User> userList = generateUserList();

        // 批量生成请求
        for (int i = 0; i < userList.size(); i++) {
            // 批量操作都在这儿
            bulkRequest.add(new IndexRequest(INDEX)
                    .id("" + (i + 1))
                    .source(JSON.toJSONString(userList.get(i)), XContentType.JSON));
        }

        BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println("批量保存结果 ---> " + bulkResponse.status());
        System.out.println("批量保存是否有失败 ---> " + bulkResponse.hasFailures());
    }

    /**
     * 名字模糊查询
     *
     * @throws IOException
     */
    @Test
    void testNameFuzzyQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest(INDEX);
        // 构建搜索条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // 查询条件可以用 QueryBuilders 构建
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
//        boolQuery.should(QueryBuilders.wildcardQuery("name", "*小刘*")); // FIXME: 能实现功能但是性能不好
        boolQuery.should(QueryBuilders.matchPhraseQuery("name", "小刘"));

        sourceBuilder.query(boolQuery);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        sourceBuilder.from(0); // 设置分页
        sourceBuilder.size(100);

        // 将查询条件放入查询请求
        searchRequest.source(sourceBuilder);

        // 执行请求
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(searchResponse);
        System.out.println(JSON.toJSONString(searchResponse.getHits()));
        System.out.println("==================遍历======================");
        System.out.println("查询到 " + searchResponse.getHits().getHits().length + " 条数据");
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
    }

    /**
     * 构造用户信息
     *
     * @return
     */
    public List<User> generateUserList() {
        List<User> userList = new ArrayList<>();
        User user1 = new User();
        user1.setName("Aleon");
        userList.add(user1);

        User user2 = new User();
        user2.setName("leon");
        userList.add(user2);

        User user3 = new User();
        user3.setName("leonB");
        userList.add(user3);

        User user4 = new User();
        user4.setName("leonjjj");
        userList.add(user4);

        // ========= 中文 =========

        User user5 = new User();
        user5.setName("刘");
        userList.add(user5);

        User user6 = new User();
        user6.setName("小刘");
        userList.add(user6);

        User user7 = new User();
        user7.setName("刘文文");
        userList.add(user7);

        User user8 = new User();
        user8.setName("这段文字里包含了刘字");
        userList.add(user8);

        User user9 = new User();
        user9.setName("刘小小");
        userList.add(user9);

        User user10 = new User();
        user10.setName("小刘小小刘小");
        userList.add(user10);

        return userList;
    }

    /**
     * 测试随机数生成
     */
    @Test
    public void testRandom() {
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            System.out.println(random.nextInt(100));
        }
    }

    /**
     * 获取文档信息
     * get/index/doc/1
     */
    @Test
    void testGetDocumentMetaTable() throws IOException {
        GetRequest getRequest = new GetRequest("meta_table", "metadata/metadata/0817tabletest111update");
        // 不获取返回的 _source 的上下文
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        System.out.println(getResponse.getSourceAsString()); // 打印文档的内容
        System.out.println(getResponse);
    }

    @Test
    void testDeleteDocumentMetaTable() throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest("meta_table", "metadata/metadata/0817tabletest111update");
        deleteRequest.timeout("1s");
        DeleteResponse deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
        System.out.println(deleteResponse);
        System.out.println(deleteResponse.status());
        System.out.println(deleteResponse.status().getStatus() == 200);
    }

    /**
     * 测试 DeleteByQuery
     *
     * @throws IOException
     */
    @Test
    void testDeleteByQuery() throws IOException {

        // 支持同时操作多个索引
        DeleteByQueryRequest request = new DeleteByQueryRequest("user");

        // 批量更新内容的时候，可能会遇到文档版本冲突的情况，需要设置版本冲突的时候如何处理
        // proceed - 忽略版本冲突，继续执行
        // abort - 遇到版本冲突，中断执行
        request.setConflicts("proceed");

        // 设置term查询条件，查询user字段=kimchy的文档内容
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.matchQuery("name", "张三0张三"));
        request.setQuery(boolQuery);

        // 限制删除文档数量
        request.setMaxDocs(Integer.MAX_VALUE);
        BulkByScrollResponse bulkResponse = client.deleteByQuery(request, RequestOptions.DEFAULT);

        // 处理结果
        TimeValue timeTaken = bulkResponse.getTook(); // 批量操作消耗时间
        System.out.println("批量操作消耗时间 timeTaken " + timeTaken);

        boolean timedOut = bulkResponse.isTimedOut(); // 是否超时
        System.out.println("是否超时 timedOut " + timedOut);

        long totalDocs = bulkResponse.getTotal(); // 涉及文档总数
        System.out.println("是否超时 totalDocs " + totalDocs);

        long deletedDocs = bulkResponse.getDeleted(); // 成功删除文档数量
        System.out.println("成功删除文档数量 deletedDocs " + deletedDocs);

        long versionConflicts = bulkResponse.getVersionConflicts(); // 版本冲突次数
        System.out.println("版本冲突次数 versionConflicts " + versionConflicts);

    }

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
//        boolQuery.must(QueryBuilders.matchQuery("name.catalogName", "metadata"));
        boolQuery.must(QueryBuilders.matchQuery("name.catalogName", "greenplum")); // 1
//        boolQuery.must(QueryBuilders.matchQuery("name.catalogName", "数据中台")); // 228
        sourceBuilder.query(boolQuery);
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
            System.out.println(hit.getSourceAsMap());
        }
    }

    /**
     * 通过数据源名称删除es数据
     *
     * @throws IOException
     */
    @Test
    void testDeleteMetadataByCatalogName() throws IOException {

        // 支持同时操作多个索引
        DeleteByQueryRequest request = new DeleteByQueryRequest("meta_table");

        // 批量更新内容的时候，可能会遇到文档版本冲突的情况，需要设置版本冲突的时候如何处理
        // proceed - 忽略版本冲突，继续执行
        // abort - 遇到版本冲突，中断执行
        request.setConflicts("proceed");

        // 设置term查询条件，查询 name.catalogName = metadata 的文档内容
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.matchQuery("name.catalogName", "greenplum"));
        request.setQuery(boolQuery);

        // 限制删除文档数量
        request.setMaxDocs(Integer.MAX_VALUE);
        BulkByScrollResponse bulkResponse = client.deleteByQuery(request, RequestOptions.DEFAULT);

        // 处理结果
        TimeValue timeTaken = bulkResponse.getTook(); // 批量操作消耗时间
        System.out.println("批量操作消耗时间 timeTaken " + timeTaken);

        boolean timedOut = bulkResponse.isTimedOut(); // 是否超时
        System.out.println("是否超时 timedOut " + timedOut);

        long totalDocs = bulkResponse.getTotal(); // 涉及文档总数
        System.out.println("是否超时 totalDocs " + totalDocs);

        long deletedDocs = bulkResponse.getDeleted(); // 成功删除文档数量
        System.out.println("成功删除文档数量 deletedDocs " + deletedDocs);

        long versionConflicts = bulkResponse.getVersionConflicts(); // 版本冲突次数
        System.out.println("版本冲突次数 versionConflicts " + versionConflicts);

    }


}
