package com.whale.oauth2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.whale.oauth2.mapper.SysUserMapper;
import com.whale.oauth2.domain.SysUser;
import com.whale.oauth2.service.SysUserService;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService{

}
