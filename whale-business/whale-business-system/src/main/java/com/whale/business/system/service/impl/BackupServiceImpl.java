package com.whale.business.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whale.business.system.domain.Backup;
import com.whale.business.system.mapper.BackupMapper;
import com.whale.business.system.service.BackupService;
import org.springframework.stereotype.Service;

/**
 * @author entfrm
 * @date 2020-03-02 16:33:24
 *
 * @description 备份Service业务层
 */
@Service
public class BackupServiceImpl extends ServiceImpl<BackupMapper, Backup> implements BackupService {

}
