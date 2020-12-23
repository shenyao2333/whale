package com.whale.provider.security.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author sy
 * @date Created in 2020.10.6 18:44
 * @description 过滤器
 */
@Slf4j
@Component
public class WhaleFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        log.info("请求路径{}",httpServletRequest.getRequestURI());
        doFilter(httpServletRequest,httpServletResponse,filterChain);

    }



}
