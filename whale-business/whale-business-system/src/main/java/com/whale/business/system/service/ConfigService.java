package com.whale.business.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.whale.business.system.domain.Config;


public interface ConfigService extends IService<Config> {
    String getValueByKey(String key);
}
