package com.whale.provider.es.cofnig;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: sy
 * @Date: Created by 2021/7/27 0027-21:42
 * @description:
 */
@Configuration
public class ElasticsearchConfig {

    @Bean
    public RestHighLevelClient restHighLevelClient(){
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        //new HttpHost("localhost", 9200, "http"),
                        new HttpHost("101.200.155.94", 9200, "http")));

        return client;
    }
}
