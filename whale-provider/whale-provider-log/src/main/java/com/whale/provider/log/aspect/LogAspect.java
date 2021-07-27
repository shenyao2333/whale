package com.whale.provider.log.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.whale.provider.log.annotation.LogRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;


/**
 * @Author: sy
 * @Date: Created by 2021/5/21 16:04
 * @description:
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogAspect {

    private final KafkaTemplate<String, Object> kafkaTemplate;


    /**
     * 以自定义 @PrintlnLog 注解作为切面入口
     */
    @Pointcut("@annotation(com.whale.provider.log.annotation.LogRecord)")
    public void printlnLog() {

    }



    @Around("@annotation(slog)")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint , LogRecord slog) throws Throwable {

        log.info("msg: {}", slog.value());
        log.info("url: {}", this.getUrl());
        String  keyValueStr =  getParamKeyValue(proceedingJoinPoint);
        log.info("参数  : {}", keyValueStr);
        log.info("Request Class and Method: {}.{}", proceedingJoinPoint.getSignature().getDeclaringTypeName(), proceedingJoinPoint.getSignature().getName());
        long startTime = System.currentTimeMillis();
        Object result = null;
        try {
            result = proceedingJoinPoint.proceed();
        }catch (Exception e){
            log.error("报错信息："+e.getMessage());
            throw e;
        }finally {
            log.info("输出结果  : {}", JSON.toJSONString(result));
            log.info("方法执行耗时: {} ms", System.currentTimeMillis() - startTime);
        }
        return result;
    }

    private String getParamKeyValue(JoinPoint proceedingJoinPoint ) {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();
        Object[] args = proceedingJoinPoint.getArgs();
        if (parameterNames!=null && parameterNames.length>0){
            JSONObject paramJson = new JSONObject();
            for (int i = 0; i < parameterNames.length; i++) {
                paramJson.put(parameterNames[i],args[i]);
            }
            return paramJson.toJSONString();
        }
        return null;
    }

    private String getUrl(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes==null){
            return null;
        }
        HttpServletRequest request = attributes.getRequest();
        StringBuffer requestUrl = request.getRequestURL();
        if (requestUrl!=null){
            return requestUrl.toString();
        }
        return null;
    }


}
