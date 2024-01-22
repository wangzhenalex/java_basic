package com.example.es7springboot;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class FlightIndexQueryTestor {
    @Test
    public void query() {
//声明RestHighLevelClient对象
        RestHighLevelClient client = null;
        try {
//********************Part1 构建客户端，创建请求 ********************
//实例化RestHighLevelClient对象,传入IP端口
            client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200)));
            //构建查询请求，指定索引为kibana_sample_data_flights
            SearchRequest request = new SearchRequest("kibana_sample_data_flights");


            //********************Part2 组织查询条件，设置排序、分页等选项 ******************

            //构建BoolQueryBuilder即布尔查询，组织多条件检索
            BoolQueryBuilder booleanQueryBuilder = QueryBuilders.boolQuery();
            //设置起始城市为威尼斯
            booleanQueryBuilder.must(QueryBuilders.termQuery("OriginCityName", "Venice"));
            //设置终点站为中国，两个条件必须同时成立，因此使用must
            booleanQueryBuilder.must(QueryBuilders.termQuery("DestCountry", "CN"));

            //利用SearchSourceBuilder构建附加选项，如排序、分页、汇总等
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            //将Boolean查询代入searchSourceBuilder
            searchSourceBuilder.query(booleanQueryBuilder);
            //分页选项，起始0行
            searchSourceBuilder.from(0); //截取前10行
            searchSourceBuilder.size(10);
            //开启命中统计（不分页时总行数），这一项设置为true，否则默认ES的记录总数上限为10000
            searchSourceBuilder.trackTotalHits(true);
            //按平均票价升序排列
            searchSourceBuilder.sort("AvgTicketPrice", SortOrder.ASC);

            //设置当前查询请求的筛选、分页、排序条件
            request.source(searchSourceBuilder);

            //********************Part3 执行查询，封装离线集合 ********************
            //执行查询，得到查询响应对象
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            //得到查询命中的文档集合
            SearchHit[] hits = response.getHits().getHits();
            //离线集合
            List<Map<String, Object>> list = new ArrayList();
            //序列化工具类
            Gson gson = new Gson();
            //遍历结果
            for (SearchHit hit : hits) {
                //得到每一份文档的JSON数据
                String json = hit.getSourceAsString();
                //利用Gson的TypeToken类将JSON反序列化为Map对象
                Map<String, Object> doc = gson.fromJson(json, new TypeToken<LinkedHashMap<String, Object>>() {
                }.getType());
                //将Map放入集合
                list.add(doc);
                //打印结果
                System.out.println(doc);
            }
            //查询不分页时命中的记录总数
            long count = response.getHits().getTotalHits().value;
            System.out.println("符合条件文档总量为：" + count);

        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            if (client != null) {
                try {
                    //********************Part4 关闭数据连接 ********************
                    //关闭客户端连接
                    client.close();

                } catch (IOException e) {
                }
            }
        }
    }

}