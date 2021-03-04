
package com.whale.provider.security.component;


import com.whale.provider.security.domain.WhaleUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * 自定义 UserAuthenticationConverter
 * @author Aijm
 * @since 2019/7/28
 */
@Slf4j
@Service
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