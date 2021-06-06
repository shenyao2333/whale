package com.whale.provider.security.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import java.util.Collection;
import java.util.List;
import java.util.Set;


/**
 * @Author: shenyao
 * @Date: Created by 2020/11/25 22:18
 * @description:
 */
public class WhaleUser  extends User {


    @Getter
    @Setter
    private String realName;


    @Getter
    @Setter
    private String avatar;


    @Getter
    @Setter
    private Integer userId;

    @Setter
    @Getter
    private Integer deptId;

    @Setter
    @Getter
    private Set<Integer> roleIds;


    public WhaleUser(Integer userId,String realName, String avatar,
                     String username ,Set<Integer> roleIds ,  Collection<? extends GrantedAuthority> authorities) {
        super(username, "" ,authorities);
        this.userId = userId;
        this.realName = realName;
        this.avatar = avatar;
        this.roleIds = roleIds;
    }


    public WhaleUser(Integer userId,String realName, String avatar,
                     String username, String password,Integer deptId,Set<Integer> roleIds,
                     Collection<? extends GrantedAuthority> authorities ) {
        super(username, password , authorities);
        this.userId = userId;
        this.realName = realName;
        this.avatar = avatar;
        this.deptId = deptId;
        this.roleIds = roleIds;
    }


    public boolean isAdmin() {
        return isAdmin(this.userId);
    }



    public static boolean isAdmin(Integer id) {
        return id != null && 1 == id;
    }


}

