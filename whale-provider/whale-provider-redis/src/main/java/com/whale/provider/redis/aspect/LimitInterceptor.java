package com.whale.provider.redis.aspect;

import com.google.common.collect.ImmutableList;

import com.whale.provider.redis.annotation.Limit;
import com.whale.provider.redis.constant.LimitType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

import static com.whale.provider.redis.constant.LimitType.*;

/**
 * @Author: sy
 * @Date: Created by 2021/5/21 11:09
 * @description: 切面
 */
@Slf4j
@Aspect
@Configuration
@RequiredArgsConstructor
public class LimitInterceptor {


    private static final String UNKNOWN = "unknown";
    private static final String CUTTING = "#";

    private final RedisTemplate<String, Object> redisTemplate;


    @Around("@annotation(limitAnnotation)")
    public Object interceptor(ProceedingJoinPoint pjp , Limit limitAnnotation) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        LimitType limitType = limitAnnotation.limitType();
        String key;
        int limitPeriod = limitAnnotation.period();
        int limitCount = limitAnnotation.count();
        /**
         * 根据限流类型获取不同的key ,如果不传我们会以方法名作为key
         */
        switch (limitType) {
            case IP:
                key = getIpAddress();
                break;
            case KEY:
                key = getKey(limitAnnotation.key(),signature.getParameterNames(),pjp.getArgs());
                break;
            default:
                key = method.getName().toUpperCase();
        }
        ImmutableList<String> keys = ImmutableList.of(key);
        try {
            String luaScript = buildLuaScript();
            RedisScript<Number> redisScript = new DefaultRedisScript<>(luaScript, Number.class);
            Number count = redisTemplate.execute(redisScript, keys, limitCount, limitPeriod);
            log.info("Access try count is {} for and key = {}", count, key);
            if (count != null && count.intValue() <= limitCount) {
                return pjp.proceed();
            } else {
                throw new RuntimeException("您已被限流！");
            }
        } catch (Throwable e) {
            if (e instanceof RuntimeException) {
                throw new RuntimeException(e.getLocalizedMessage());
            }
            throw new RuntimeException("server exception");
        }
    }

    private String getKey(String key, String[] parameterNames, Object[] args) {
        if (!key.contains(CUTTING)){
            return key;
        }
        String[] split = key.split(CUTTING);
        String variable = split[1];
        for (int i = 0; i < parameterNames.length; i++) {
            if (parameterNames[i].equals(variable)){
                return split[0] + args[i];
            }
        }
        return key;
    }


    public String buildLuaScript() {
        StringBuilder lua = new StringBuilder();
        lua.append("local c");
        lua.append("\nc = redis.call('get',KEYS[1])");
        // 调用不超过最大值，则直接返回
        lua.append("\nif c and tonumber(c) > tonumber(ARGV[1]) then");
        lua.append("\nreturn c;");
        lua.append("\nend");
        // 执行计算器自加
        lua.append("\nc = redis.call('incr',KEYS[1])");
        lua.append("\nif tonumber(c) == 1 then");
        // 从第一次调用开始限流，设置对应键值的过期
        lua.append("\nredis.call('expire',KEYS[1],ARGV[2])");
        lua.append("\nend");
        lua.append("\nreturn c;");
        return lua.toString();
    }


    public String getIpAddress() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


}
