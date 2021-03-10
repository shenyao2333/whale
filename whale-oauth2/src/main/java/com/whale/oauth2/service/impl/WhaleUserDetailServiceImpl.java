package com.whale.oauth2.service.impl;

import com.whale.oauth2.domain.SecurityUser;
import com.whale.oauth2.domain.SysUser;
import com.whale.oauth2.service.SysUserService;
import com.whale.oauth2.service.WhaleUserDetailService;
import com.whale.provider.basices.redis.RedisUtil;
import com.whale.provider.basices.web.GrabException;
import com.whale.provider.common.constant.SysConstant;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

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


    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        List<SysUser> sysUsers = sysUserService.selectByUserName(userName);
        if (sysUsers!=null&&sysUsers.size()>0){
            SysUser sysUser = sysUsers.get(0);
            if (sysUser.getType()==0){
                return new SecurityUser(sysUser.getId(),sysUser.getRealName(),sysUser.getAvatar(),sysUser.getUserName(),sysUser.getPassword(),AuthorityUtils.NO_AUTHORITIES);
            }
        }
        throw new UsernameNotFoundException("账号不存在！");
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
            throw new UsernameNotFoundException("手机号错误！");
        }
        SysUser sysUser = sysUsers.get(0);
        if (sysUser.getType()==0){
            return new SecurityUser(sysUser.getId(),sysUser.getRealName(),sysUser.getAvatar(),sysUser.getUserName(),sysUser.getPassword(),AuthorityUtils.NO_AUTHORITIES);
        }
        throw new GrabException(3002,"手机号错误！");
    }




}
