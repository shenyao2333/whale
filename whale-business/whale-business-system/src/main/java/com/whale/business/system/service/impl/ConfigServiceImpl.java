package com.whale.business.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whale.business.system.domain.Config;
import com.whale.business.system.mapper.ConfigMapper;
import com.whale.business.system.service.ConfigService;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService {

    private final RedisTemplate redisTemplate;

    @Override
    public String getValueByKey(String key) {
        String value = "";
        Object valStr = redisTemplate.opsForValue().get(key);
        if (!StrUtil.isEmptyIfStr(valStr)) {
            value = valStr.toString();
        } else {
            Config config = baseMapper.selectOne(new QueryWrapper<Config>().eq("`key`", key));
            if(config != null){
                value = config.getValue();
                redisTemplate.opsForValue().set(key, config.getValue());
            }

       }
        return value;
    }
}
