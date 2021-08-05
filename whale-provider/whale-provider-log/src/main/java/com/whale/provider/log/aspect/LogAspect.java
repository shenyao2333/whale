package com.whale.provider.log.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.whale.provider.es.constant.LogInfoEs;
import com.whale.provider.kafka.constant.KafkaTopicConstant;
import com.whale.provider.log.annotation.LogRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * @Author: sy
 * @Date: Created by 2021/5/21 16:04
 * @description:
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;


    @Resource
    @Qualifier(value = "threadPool")
    private ExecutorService threadPoolExecutor;


    @Value("${spring.application.name}")
    private String moduleName;


    /**
     * 以自定义 @PrintlnLog 注解作为切面入口
     */
    @Pointcut("@annotation(com.whale.provider.log.annotation.LogRecord)")
    public void printlnLog() {

    }



    @Around("@annotation(slog)")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint , LogRecord slog) throws Throwable {
        LogInfoEs logInfo = new LogInfoEs();
        logInfo.setValue(slog.value());
        logInfo.setUrl(this.getUrl());
        String  keyValueStr =  getParamKeyValue(proceedingJoinPoint);
        logInfo.setParam(keyValueStr);
        logInfo.setClassName(proceedingJoinPoint.getSignature().getDeclaringTypeName());
        logInfo.setMethodName(proceedingJoinPoint.getSignature().getName());
        logInfo.setModuleName(moduleName);
        long startTime = System.currentTimeMillis();
        Object result = null;
        try {
            result = proceedingJoinPoint.proceed();
        }catch (Exception e){
            logInfo.setErrorMsg(e.getMessage());
            throw e;
        }finally {
            logInfo.setReturnResult(JSON.toJSONString(result));
            logInfo.setElapsedTime(System.currentTimeMillis() - startTime);
            this.sendKafka(logInfo);
        }
        return result;
    }


    private void sendKafka(LogInfoEs logInfo){
        String json = JSONObject.toJSONString(logInfo);
        log.info("进入kafka-->"+json);
        threadPoolExecutor.execute(()->
                kafkaTemplate.send(KafkaTopicConstant.LOG,json) );
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
