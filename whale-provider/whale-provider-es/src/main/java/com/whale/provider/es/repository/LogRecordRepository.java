package com.whale.provider.es.repository;

import com.whale.provider.es.constant.LogRecordInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: sy
 * @Date: Created by 2021/8/10 9:52
 * @description:
 */
@Repository
public interface LogRecordRepository extends ElasticsearchRepository<LogRecordInfo,String> {




}
