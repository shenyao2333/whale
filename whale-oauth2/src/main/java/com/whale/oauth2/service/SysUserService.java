package com.whale.oauth2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.whale.oauth2.domain.SysUser;

import java.util.List;


public interface SysUserService extends IService<SysUser> {

    List<SysUser> selectByUserName(String userName);



}
