package com.whale.oauth2.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * @author sy
 * @date: 2021/3/3 11:43
 * @description
 */
public class SecurityUser extends User {

    @Getter
    @Setter
    private String realName;


    @Getter
    @Setter
    private String avatar;


    @Getter
    @Setter
    private Integer userId;



    public SecurityUser(Integer userId,String realName, String avatar,
                     String username) {
        super(username, "" ,  AuthorityUtils.NO_AUTHORITIES);
        this.userId = userId;
        this.realName = realName;
        this.avatar = avatar;
    }


    public SecurityUser(Integer userId,String realName, String avatar,
                     String username, String password,
                     Collection<? extends GrantedAuthority> authorities) {
        super(username, password , authorities);
        this.userId = userId;
        this.realName = realName;
        this.avatar = avatar;
    }


    public boolean isAdmin() {
        return isAdmin(this.userId);
    }



    public static boolean isAdmin(Integer id) {
        return id != null && 1 == id;
    }



}
