package com.whale.provider.es.service.impl;

import com.whale.provider.es.service.SearchRequestService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: sy
 * @Date: Created by 2021/8/10 14:39
 * @description:
 */
@Service
@AllArgsConstructor
public class SearchRequestServiceImpl implements SearchRequestService {

    private final RestHighLevelClient client;
    private final ElasticsearchTemplate elasticsearchTemplate;


    @SneakyThrows
    @Override
    public Object search(Integer pageNum,Integer pageSize,String keyword) {

       // elasticsearchTemplate.

        SearchRequest searchRequest = new SearchRequest("company");

        // SearchSourceBuilder 是构建搜索条件的盒子。
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        NativeSearchQueryBuilder searchQuery = new NativeSearchQueryBuilder();

        //设置高亮的字段，也可以不设置。
        HighlightBuilder fieldList = new HighlightBuilder().field("value").field("errorMsg").field("param").field("returnResult");
        fieldList.preTags("<b style='color:red'>");
        fieldList.postTags("</b>");
        sourceBuilder.highlighter(fieldList);




        //设置分页。
        sourceBuilder.from(pageNum);
        sourceBuilder.size(pageSize);


        /**
         * 常用的查询方式有：
         * .matchQuery() 这个是模糊查询
         * .matchAllQuery() 查询全部
         * .termQuery() 精确查询。
         */
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders
                .multiMatchQuery("firstName", "小明", "sdf");

     //   QueryBuilders.boolQuery().
      //  searchQuery.withQuery(multiMatchQueryBuilder).

        //设置响应超时时间
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        sourceBuilder.query(multiMatchQueryBuilder);
        searchRequest.source(sourceBuilder);


        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);


        ArrayList<Map<String, Object>> maps = new ArrayList<>();
        for (SearchHit hit: searchResponse.getHits().getHits()){
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField title = highlightFields.get("firstName");
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            if (title!=null){
                StringBuffer sb = new StringBuffer();
                Text[] fragments = title.fragments();
                for (Text s : fragments){
                    sb.append(s);
                }
                sourceAsMap.put("firstName",sb);
            }
            maps.add(sourceAsMap);
        }
        return maps;
    }
}
