package com.whale.provider.es.service;

/**
 * @Author: sy
 * @Date: Created by 2021/8/10 14:38
 * @description:
 */
public interface SearchRequestService {

    Object search(Integer pageNum,Integer pageSize,String keyword,String moduleName);

}
