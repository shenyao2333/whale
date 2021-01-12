package com.whale.provider.security.utils;


import com.whale.provider.security.domian.WhaleUser;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


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
		return (WhaleUser) authentication.getPrincipal();
	}


}
