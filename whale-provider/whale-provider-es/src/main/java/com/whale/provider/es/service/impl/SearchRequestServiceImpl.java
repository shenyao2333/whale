package com.whale.provider.es.service.impl;

import cn.hutool.core.util.StrUtil;
import com.whale.provider.es.constant.LogRecordInfo;
import com.whale.provider.es.repository.LogRecordRepository;
import com.whale.provider.es.service.SearchRequestService;
import com.whale.provider.es.utils.HighlightUtil;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * @Author: sy
 * @Date: Created by 2021/8/10 14:39
 * @description:
 */
@Service
@AllArgsConstructor
public class SearchRequestServiceImpl implements SearchRequestService {

    private final LogRecordRepository logRecordRepository;
    private final ElasticsearchRestTemplate elasticsearchRestTemplate;


    @SneakyThrows
    @Override
    public Object search(Integer pageNum,Integer pageSize,String keyword,String moduleName) {


        Pageable pageable = PageRequest.of(pageNum, pageSize);


        NativeSearchQueryBuilder searchQuery = new NativeSearchQueryBuilder();

       searchQuery.withPageable(pageable);

        //设置高亮的字段，也可以不设置。
        HighlightBuilder fieldList = new HighlightBuilder().field("moduleName")
                .field("value").field("errorMsg").field("param").field("returnResult");
        fieldList.preTags("<b style='color:red'>");
        fieldList.postTags("</b>");
        searchQuery.withHighlightBuilder(fieldList);

        BoolQueryBuilder should = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("moduleName", moduleName))
               .should(QueryBuilders.matchQuery("value", keyword)).should(QueryBuilders.matchQuery("param", keyword));
        searchQuery.withQuery(should);
        SearchHits<LogRecordInfo> search = elasticsearchRestTemplate.search(searchQuery.build(), LogRecordInfo.class,IndexCoordinates.of("log_record-2021-08"));
        ArrayList<LogRecordInfo> assignment = HighlightUtil.assignment(search);
        long totalHits = search.getTotalHits();
        return assignment;
    }

    @Override
    public Object search2(int i, int i1, String keyword, String moduleName) {
        Pageable pageable = PageRequest.of(i, i1);

        NativeSearchQueryBuilder searchQuery = new NativeSearchQueryBuilder();

        searchQuery.withPageable(pageable);

        if (StrUtil.isNotBlank(keyword)){
            //设置高亮的字段，也可以不设置。
            HighlightBuilder fieldList = new HighlightBuilder().field("moduleName")
                    .field("value").field("errorMsg").field("param").field("returnResult");
            fieldList.preTags("<b style='color:red'>");
            fieldList.postTags("</b>");
            searchQuery.withHighlightBuilder(fieldList);

            BoolQueryBuilder should = QueryBuilders.boolQuery()
                    .must(QueryBuilders.termQuery("moduleName", moduleName))
                    .should(QueryBuilders.matchQuery("value", keyword).boost(2))
                    .should(QueryBuilders.matchQuery("param", keyword));
            searchQuery.withQuery(should);
        }else {
            searchQuery.withQuery(QueryBuilders.matchAllQuery());
        }

        SearchHits<LogRecordInfo> search = elasticsearchRestTemplate.search(searchQuery.build(), LogRecordInfo.class,IndexCoordinates.of("log_record-2021-08"));
        ArrayList<LogRecordInfo> assignment = HighlightUtil.assignment(search);
        long totalHits = search.getTotalHits();
        return search;
    }


    public void sdf(String keyword){
        NativeSearchQueryBuilder searchQuery = new NativeSearchQueryBuilder();

        List<FunctionScoreQueryBuilder.FilterFunctionBuilder> filterFunctionBuilders = new ArrayList<>();
        filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.matchQuery("moduleName", keyword),
                ScoreFunctionBuilders.weightFactorFunction(10)));
        filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.matchQuery("value", keyword),
                ScoreFunctionBuilders.weightFactorFunction(5)));
        filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.matchQuery("param", keyword),
                ScoreFunctionBuilders.weightFactorFunction(2)));
        FunctionScoreQueryBuilder.FilterFunctionBuilder[] builders = new FunctionScoreQueryBuilder.FilterFunctionBuilder[filterFunctionBuilders.size()];
        filterFunctionBuilders.toArray(builders);
        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(builders)
                .scoreMode(FunctionScoreQuery.ScoreMode.SUM)
                .setMinScore(2);
        searchQuery.withQuery(functionScoreQueryBuilder);

    }
}
