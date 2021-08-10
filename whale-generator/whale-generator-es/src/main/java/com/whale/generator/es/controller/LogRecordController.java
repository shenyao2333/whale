package com.whale.generator.es.controller;

import com.whale.provider.basices.web.R;
import com.whale.provider.es.constant.LogRecordInfo;
import com.whale.provider.es.repository.LogRecordRepository;
import com.whale.provider.es.service.TestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: sy
 * @Date: Created by 2021/8/10 9:46
 * @description: 操作日志
 */
@RestController
@RequestMapping("/logRecord")
public class LogRecordController {

    @Resource
    private  LogRecordRepository logRecordRepository;
    @Resource
    private TestService testService;

    @GetMapping("/testSave")
    public R list(){
        LogRecordInfo logRecordInfo = new LogRecordInfo();
        logRecordInfo.setModuleName("es");
        logRecordInfo.setValue("我在测试的哦");
        logRecordInfo.setUrl("123123");
        logRecordInfo.setParam("sdfsdfsdf");
        logRecordInfo.setClassName("cdfs");
        logRecordInfo.setMethodName("cdfs");
        logRecordInfo.setErrorMsg("报错信息，不能为空");
        logRecordInfo.setReturnResult("cdfs");
        logRecordInfo.setElapsedTime(200L);
        logRecordRepository.save(logRecordInfo);
        return R.ok();
    }


}
