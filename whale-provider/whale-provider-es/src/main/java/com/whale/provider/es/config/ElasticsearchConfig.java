package com.whale.provider.es.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * @Author: sy
 * @Date: Created by 2021/7/27 0027-21:42
 * @description:
 */

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.whale")
public class ElasticsearchConfig {

  // @Bean
  // public RestHighLevelClient restHighLevelClient(){
  //     return new RestHighLevelClient(
  //             RestClient.builder(
  //                     //new HttpHost("localhost", 9200, "http"),
  //                     new HttpHost("123.56.97.130", 9200, "http")));
  // }
}
