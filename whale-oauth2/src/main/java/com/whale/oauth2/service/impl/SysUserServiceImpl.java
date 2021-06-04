package com.whale.oauth2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.whale.oauth2.mapper.SysUserMapper;
import com.whale.oauth2.domain.SysUser;
import com.whale.oauth2.service.SysUserService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService{

    private final SysUserMapper sysUserMapper;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<SysUser> selectByUserName(String userName) {
        return sysUserMapper.selectByUserName(userName);
    }


    @Override
    public List<SysUser> selectByPhone(String phone) {
        return sysUserMapper.selectByPhone(phone);
    }

}
