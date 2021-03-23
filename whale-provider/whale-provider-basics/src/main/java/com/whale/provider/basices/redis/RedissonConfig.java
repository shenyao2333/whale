package com.whale.provider.basices.redis;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.redisson.config.Config;
/**
 * @Author: shenyao
 * @Date: Created by 2021/3/23 21:02
 * @description: 分布式锁使用
 */
@Configuration
public class RedissonConfig {

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private String port;
    @Value("${spring.redis.password}")
    private String password;


    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        String redisUrl = String.format("redis://%s:%s",host+"",port);
        config.useSingleServer().setAddress(redisUrl).setPassword(password);
        config.useSingleServer().setDatabase(3);
        return Redisson.create(config);
    }


    private   RedissonClient redissonClient;

    public void sd(){
        RLock s = redissonClient.getLock("s");

    }


}
