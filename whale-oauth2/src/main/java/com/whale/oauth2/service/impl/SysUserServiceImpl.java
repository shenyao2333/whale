package com.whale.oauth2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.whale.oauth2.mapper.SysUserMapper;
import com.whale.oauth2.domain.SysUser;
import com.whale.oauth2.service.SysUserService;

import java.util.List;

@Service
@AllArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService{

    private final SysUserMapper sysUserMapper;

    @Override
    public List<SysUser> selectByUserName(String userName) {
        return sysUserMapper.selectByUserName(userName);
    }
}
