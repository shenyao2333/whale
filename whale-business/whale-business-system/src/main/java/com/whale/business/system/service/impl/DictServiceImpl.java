package com.whale.business.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.whale.business.system.domain.Dict;
import com.whale.business.system.mapper.DictMapper;
import com.whale.business.system.service.DictService;
import org.springframework.stereotype.Service;


@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

}
