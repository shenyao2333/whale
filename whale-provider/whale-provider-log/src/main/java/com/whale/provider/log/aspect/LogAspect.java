package com.whale.provider.log.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.whale.provider.kafka.constant.KafkaTopicConstant;
import com.whale.provider.log.annotation.LogRecord;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;


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

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");

    /**
     * 以自定义 @PrintlnLog 注解作为切面入口
     */
    @Pointcut("@annotation(com.whale.provider.log.annotation.LogRecord)")
    public void printlnLog() {

    }



    @Around("@annotation(slog)")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint , LogRecord slog) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = null;
        String errMsg=" ";
        try {
            return  result = proceedingJoinPoint.proceed();
        }catch (Exception e){
            errMsg = e.getMessage();
            throw e;
        }finally {
            String strBuff = moduleName + "|" + slog.value() + "|" + this.getUrl() + "|" +
                    this.getParamKeyValue(proceedingJoinPoint) + "|" +
                    proceedingJoinPoint.getSignature().getDeclaringTypeName() + "|" +
                    proceedingJoinPoint.getSignature().getName() + "|" +
                    JSON.toJSONString(result) + "|" +
                    (System.currentTimeMillis() - startTime) + "|" +
                    errMsg + "|" +dateFormat.format(new Date());
            this.sendKafka(strBuff);
        }
    }


    private void sendKafka(String logString){
        log.info("进入kafka-->"+logString);
        threadPoolExecutor.execute(()->
                kafkaTemplate.send(KafkaTopicConstant.LOG,logString) );
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
