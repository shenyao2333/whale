package com.whale.oauth2.service;

import com.whale.oauth2.domain.SysUser;
import com.whale.provider.basices.domain.WhaleUser;
import com.whale.provider.basices.web.GrabException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;

/**
 * @author sy
 * @date Created in 2020.10.6 18:29
 * @description 用户名登录逻辑
 */
@Service
@AllArgsConstructor
public class WhaleUserDetailService implements UserDetailsService {


    private final SysUserService sysUserService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        List<SysUser> sysUsers = sysUserService.selectByUserName(userName);
        if (sysUsers!=null&&sysUsers.size()>0){
            SysUser sysUser = sysUsers.get(0);
            if (sysUser.getType()==0){
                return new WhaleUser(sysUser.getId(),sysUser.getRealName(),sysUser.getAvatar(),sysUser.getUserName(),sysUser.getPassword(),true, true, true,false,AuthorityUtils.createAuthorityList("user"));
            }
        }
        throw new GrabException(2001,"用户名或密码错误");
    }



   /* public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("123456"));
    }*/


    public static void main(String[] args) {

        String namePwd="whale:123456";


        byte[] bytes = namePwd.getBytes();
        String Basic = Base64.getEncoder().encodeToString(bytes);
        System.out.println(Basic);
    }




}
