package com.whale.provider.security.utils;



import cn.hutool.core.util.StrUtil;
import com.whale.provider.security.domain.WhaleUser;
import lombok.experimental.UtilityClass;
import org.apache.dubbo.common.constants.CommonConstants;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * @author sy
 * @date 2020/3/10
 * @description 安全工具类
 */
@UtilityClass
public class SecurityUtil {



	/**
	 * 获取Authentication
	 */
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	/**
	 * 获取用户
	 */
	public WhaleUser getUser() {

		Authentication authentication = getAuthentication();
		if (authentication==null){
			return null;
		}

		return (WhaleUser)authentication.getPrincipal();
	}


	/**
	 * 获取用户角色信息
	 *
	 * @return 角色集合
	 */
	public List<Integer> getRoles() {
		Authentication authentication = getAuthentication();
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

		List<Integer> roleIds = new ArrayList<>();
		authorities.stream()
				.filter(granted -> StrUtil.startWith(granted.getAuthority(),"ROLE_"))
				.forEach(granted -> {
					String id = StrUtil.removePrefix(granted.getAuthority(), "ROLE_");
					roleIds.add(Integer.parseInt(id));
				});
		return roleIds;
	}


}
