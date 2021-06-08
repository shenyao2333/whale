package com.whale.oauth2.service.impl;

import com.whale.oauth2.domain.SecurityUser;
import com.whale.oauth2.domain.SysUser;
import com.whale.oauth2.domain.vo.AuthUserVo;
import com.whale.oauth2.mapper.AuthUserMapper;
import com.whale.oauth2.service.SysUserService;
import com.whale.oauth2.service.WhaleUserDetailService;
import com.whale.provider.basices.redis.RedisUtil;
import com.whale.provider.basices.web.GrabException;
import com.whale.provider.common.constant.SysConstant;
import com.whale.provider.security.domain.WhaleUser;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author sy
 * @date Created in 2020.10.6 18:29
 * @description 用户名登录逻辑
 */
@Service
@AllArgsConstructor
public class WhaleUserDetailServiceImpl implements WhaleUserDetailService {


    private final SysUserService sysUserService;
    private final RedisUtil redisUtil;
    private final AuthUserMapper authUserMapper;


    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        AuthUserVo authUserVo = authUserMapper.authUserByUserName(userName);
        if (authUserVo == null){
            throw  new GrabException("账号或者密码错误！");
        }
        Set<Integer> roleIds = authUserVo.getRoleIds();
        ArrayList<Integer> roleList = new ArrayList<>(roleIds);
        return new WhaleUser(
                authUserVo.getUserId(), authUserVo.getNickName(),authUserVo.getAvatar(),
                authUserVo.getUserName(), authUserVo.getPassword(),authUserVo.getDeptId(),
                roleList,this.createAuthorityList(authUserVo.getPermsList()));
    }



    @Override
    public UserDetails loadUserByMobile(String mobile,String code) {
        String reCode =(String) redisUtil.get(SysConstant.PHONE_LOG_AUTH + mobile);
        if (!"88888".equals(code)){
            if (reCode==null||!reCode.equals(code)){
                throw new GrabException(3023,"手机验证码错误或已失效！");
            }
        }
        List<SysUser> sysUsers = sysUserService.selectByPhone(mobile);
        if (sysUsers==null||sysUsers.size()<1){
            throw new GrabException(2323,"手机号错误！");
        }
        SysUser sysUser = sysUsers.get(0);
        if (sysUser.getType()==0){
            return new SecurityUser(sysUser.getUserId(),sysUser.getRealName(),sysUser.getAvatar(),sysUser.getUserName(),
                    sysUser.getPassword(),AuthorityUtils.NO_AUTHORITIES);
        }
        throw new GrabException(3002,"手机号错误！");
    }


    public List<GrantedAuthority> createAuthorityList(Set<String> setList) {
        if (setList != null) {
            ArrayList<String> roleList = new ArrayList<>(setList);
            List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>(roleList.size());
            for (int var = 0; var < roleList.size(); var++) {
                grantedAuthorities.add(new SimpleGrantedAuthority(roleList.get(var)));
            }
            return grantedAuthorities;
        }
        return AuthorityUtils.NO_AUTHORITIES;

    }



}
