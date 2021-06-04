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
    private String nickName;


    @Getter
    private String avatar;


    @Getter
    private Integer userId;

    @Getter
    private Integer deptId;

    @Getter
    private String userName;

    public SecurityUser(Integer userId,String nickName, String avatar,
                     String userName) {
        super(userName, "" ,  AuthorityUtils.NO_AUTHORITIES);
        this.userId = userId;
        this.nickName = nickName;
        this.avatar = avatar;
        this.userName = userName;
    }


    public SecurityUser(Integer userId,String nickName, String avatar,
                     String username, String password,
                     Collection<? extends GrantedAuthority> authorities) {
        super(username, password , authorities);
        this.userId = userId;
        this.nickName = nickName;
        this.avatar = avatar;
        this.userName = userName;
    }





}
