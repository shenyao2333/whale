package com.whale.provider.security.exception;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.JSONObject;
import com.whale.provider.basices.web.R;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义Token异常信息
 * @author Aijm
 * @since 2019/11/20
 */
@Slf4j
@Component
@AllArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {


	@Override
	@SneakyThrows
	public void commence(HttpServletRequest request, HttpServletResponse response,
						 AuthenticationException authException) {
		log.error("{},匿名用户访问无权限:{}",request.getRequestURI(), authException.getMessage());
		response.setCharacterEncoding(CharsetUtil.UTF_8);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpStatus.HTTP_OK);
		response.getWriter()
				.print(JSONObject.toJSONString(R.fail(R.CodeStatus.POWER,"请先登录！")));
	}
}