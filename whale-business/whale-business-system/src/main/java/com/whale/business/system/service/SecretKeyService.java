package com.whale.business.system.service;

import com.whale.business.system.domain.vo.OssSTSVo;

/**
 * @Author: shenyao
 * @Date: Created by 2020/12/23 20:42
 * @description: 密钥处理
 */
public interface SecretKeyService{
    /**
     * 获取STS临时授权访问OSS
     * @return
     */
    OssSTSVo getOssSTS();

}
