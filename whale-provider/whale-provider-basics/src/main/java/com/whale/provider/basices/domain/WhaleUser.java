package com.whale.provider.basices.domain;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;


/**
 * @Author: shenyao
 * @Date: Created by 2020/11/25 22:18
 * @description:
 */
public class WhaleUser  extends User {


    /**
     * 真实姓名
     */
    @Getter
    private String realName;

    /**
     * 头像
     */
    @Getter
    private String avatar;

    /**
     * userId
     */
    @Getter
    private Integer userId;


    public WhaleUser(Integer userId,String realName, String avatar, String username, String password,  Collection<? extends GrantedAuthority> authorities) {
        super(username, password , authorities);
        this.userId = userId;
        this.realName = realName;
        this.avatar = avatar;

    }

   /* public boolean isAdmin() {
        return isAdmin(this.userId);
    }

    public static boolean isAdmin(Integer id) {
        return id != null && 1 == id;
    }*/

}
