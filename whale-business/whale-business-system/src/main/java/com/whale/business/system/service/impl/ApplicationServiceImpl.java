package com.whale.business.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.whale.business.system.domain.Application;
import com.whale.business.system.mapper.ApplicationMapper;
import com.whale.business.system.service.ApplicationService;
import org.springframework.stereotype.Service;

/**
 * @author entfrm
 * @date 2020-04-23 18:15:10
 * @description 应用Service业务层
 */
@Service
public class ApplicationServiceImpl extends ServiceImpl<ApplicationMapper, Application> implements ApplicationService {
}
