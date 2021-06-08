
package com.whale.provider.security.component;


import com.whale.provider.security.domain.WhaleUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * @Author: sy
 * @Date: Created by 2021/5/25 14:35
 * @description: 定义转化 authentication.getPrincipal() 获取到的数据
 */
@Slf4j
@Service
@Primary
public class WhaleUserAuthenticationConverter extends DefaultUserAuthenticationConverter {


    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {
        if (map.containsKey("userName")) {
            String userName =(String) map.get("userName");
            Integer userId = (Integer) map.get("userId");
            String avatar = (String) map.get("avatar");
            ArrayList<Integer> roleIds = (ArrayList<Integer>) map.get("roleIds");
            ArrayList<String> authorities = (ArrayList<String>) map.get("authorities");
            List<GrantedAuthority> authorityList = createAuthorityList(authorities);
            WhaleUser user = new WhaleUser(userId,userName,avatar,userName,roleIds,authorityList);
            return new UsernamePasswordAuthenticationToken(user, "N/A", authorityList);
        } else {
            return null;
        }
    }

    public List<GrantedAuthority> createAuthorityList(List<String> setList) {
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