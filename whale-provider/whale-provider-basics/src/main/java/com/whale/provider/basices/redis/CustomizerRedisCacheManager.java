package com.whale.provider.basices.redis;

import org.springframework.data.redis.cache.*;
import org.springframework.util.StringUtils;

import java.time.Duration;

/**
 * @Author: shenyao
 * @Date: Created by 2021/1/3 19:23
 * @description: Cache能自定义每个key的过期时间
 */
public class CustomizerRedisCacheManager extends RedisCacheManager {
    /**
     * 缓存参数的分隔符
     * 数组元素0=缓存的名称
     * 数组元素1=缓存过期时间TTL
     */
    private static final String DEFAULT_SEPARATOR = "#";


    public CustomizerRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
        super(cacheWriter, defaultCacheConfiguration);
    }

    @Override
    protected RedisCache createRedisCache(String name, RedisCacheConfiguration cacheConfig) {
        Duration ttl = getTtlByName(name);
        if (ttl != null) {
            //证明在cacheName上使用了过期时间，需要修改配置中的ttl
            cacheConfig = cacheConfig.entryTtl(ttl);
        }
        cacheConfig = cacheConfig.computePrefixWith(DEFAULT_CACHE_KEY_PREFIX);
        if (name.indexOf(DEFAULT_SEPARATOR)>0){
            name = name.substring(0,name.indexOf(DEFAULT_SEPARATOR));
        }
        return super.createRedisCache(name, cacheConfig);
    }



    /**
     * 通过name获取过期时间
     * @param name
     * @return
     */
    private Duration getTtlByName(String name) {
        if (name == null) {
            return null;
        }
        //根据分隔符拆分字符串，并进行过期时间ttl的解析
        String[] cacheParams = name.split(DEFAULT_SEPARATOR);
        if (cacheParams.length > 1) {
            String ttl = cacheParams[1];
            if (!StringUtils.isEmpty(ttl)) {
                try {
                    return Duration.ofSeconds(Long.parseLong(ttl));
                } catch (Exception e) {
                }
            }
        }
        return null;
    }

    /**
     * 默认的key前缀
     */
    private static final CacheKeyPrefix DEFAULT_CACHE_KEY_PREFIX = new CacheKeyPrefix() {
        @Override
        public String compute(String cacheName) {
            return cacheName+":";
        }
    };

}
