package com.whale.business.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.whale.business.system.domain.OperLog;
import com.whale.business.system.mapper.OperLogMapper;
import com.whale.business.system.service.OperLogService;
import org.springframework.stereotype.Service;


@Service
public class OperLogServiceImpl extends ServiceImpl<OperLogMapper, OperLog> implements OperLogService {

}
