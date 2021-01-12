/**
 *    https://gitee.com/gitsc/pro-cloud/
 *     @Author Aijm 2929793435@qq.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.whale.provider.security.component;


import com.cloud.common.util.oauth.AdditionalToken;
import com.whale.provider.security.domian.WhaleUser;
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
@Service
public class WhaleUserAuthenticationConverter extends DefaultUserAuthenticationConverter {


    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {
        if (map.containsKey("user_name")) {
            Object principal = map.get("user_name");
           //Object obj = map.get(AdditionalToken.USER_ID);
           //Long userId = Long.valueOf(String.valueOf(obj));
           //Object objType = map.get(AdditionalToken.USER_TYPE);
           //Object tenantId = map.get(AdditionalToken.TENANT_ID);
            WhaleUser user = new WhaleUser(1,"WhaleUser","","");
            return new UsernamePasswordAuthenticationToken(user, "N/A", AuthorityUtils.NO_AUTHORITIES);
        } else {
            return null;
        }
    }


    private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {
        Object authorities = map.get("authorities");
        if (authorities instanceof String) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList((String)authorities);
        } else if (authorities instanceof Collection) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList(StringUtils.collectionToCommaDelimitedString((Collection)authorities));
        } else {
            throw new IllegalArgumentException("Authorities must be either a String or a Collection");
        }
    }
}