/*
package com.whale.oauth2.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

*/
/**
 * @Author: shenyao
 * @Date: Created by 2021/1/5 21:08
 * @description: 实现oauth2 的 读取客户端
 *//*

@Service
public class WhaleJdbcClientDetailsService extends JdbcClientDetailsService {

    public WhaleJdbcClientDetailsService(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    @Cacheable( value = "oauth#3600" ,key = "#clientId")
    public ClientDetails loadClientByClientId(String clientId) throws InvalidClientException {
        return super.loadClientByClientId(clientId);
    }
}
*/
