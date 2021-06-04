package com.whale.business.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.entfrm.base.config.GlobalConfig;
import com.entfrm.base.util.StrUtil;
import com.entfrm.biz.system.entity.Config;
import com.entfrm.biz.system.mapper.ConfigMapper;
import com.entfrm.biz.system.service.ConfigService;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 参数配置表 服务实现类
 * </p>
 *
 * @author entfrm
 * @since 2019-01-30
 */
@Service
@AllArgsConstructor
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService {

    private final RedisTemplate redisTemplate;

    @Override
    public String getValueByKey(String key) {
        String value = "";
        //redis缓存
        if(GlobalConfig.isRedisSwitch()){
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
        }else {
            Config config = baseMapper.selectOne(new QueryWrapper<Config>().eq("`key`", key));
            if(config != null){
                value = config.getValue();
            }
        }
        return value;
    }
}
