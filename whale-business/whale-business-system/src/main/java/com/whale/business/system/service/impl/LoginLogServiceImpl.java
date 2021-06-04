package com.whale.business.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.whale.business.system.domain.LoginLog;
import com.whale.business.system.mapper.LoginLogMapper;
import com.whale.business.system.service.LoginLogService;
import org.springframework.stereotype.Service;


@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {

}
