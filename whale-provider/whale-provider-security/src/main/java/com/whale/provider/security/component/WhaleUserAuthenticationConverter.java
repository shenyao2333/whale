
package com.whale.provider.security.component;


import com.whale.provider.security.domain.WhaleUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;



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
            WhaleUser user = new WhaleUser(userId,userName,avatar,userName);
            return new UsernamePasswordAuthenticationToken(user, "N/A", AuthorityUtils.NO_AUTHORITIES);
        } else {
            return null;
        }
    }


}