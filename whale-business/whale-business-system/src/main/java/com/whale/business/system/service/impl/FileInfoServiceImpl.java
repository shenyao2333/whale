package com.whale.business.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.whale.business.system.domain.FileInfo;
import com.whale.business.system.mapper.FileInfoMapper;
import com.whale.business.system.service.FileInfoService;
import org.springframework.stereotype.Service;


@Service
public class FileInfoServiceImpl extends ServiceImpl<FileInfoMapper, FileInfo> implements FileInfoService {

}
