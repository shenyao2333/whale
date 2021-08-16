package com.whale.generator.es.controller;

import com.whale.provider.basices.web.R;
import com.whale.provider.es.constant.LogRecordInfo;
import com.whale.provider.es.repository.LogRecordRepository;
import com.whale.provider.es.service.SearchRequestService;
import com.whale.provider.es.service.TestService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Author: sy
 * @Date: Created by 2021/8/10 9:46
 * @description: 操作日志
 */
@RestController
@RequestMapping("/logRecord")
@AllArgsConstructor
public class LogRecordController {


    private final LogRecordRepository logRecordRepository;
    private final  SearchRequestService searchRequestService;

    @GetMapping("/testSave")
    public R list(){
        LogRecordInfo logRecordInfo = new LogRecordInfo();
        logRecordInfo.setModuleName("es");
        logRecordInfo.setValue("这个星期我准备去北京玩耍， 不知道你想去吗？");
        logRecordInfo.setUrl("123123");
        logRecordInfo.setParam("我是效名");
        logRecordInfo.setClassName("cdfs");
        logRecordInfo.setMethodName("cdfs");
        logRecordInfo.setErrorMsg("报错信息，不能为空");
        logRecordInfo.setReturnResult("cdfs");
        logRecordInfo.setElapsedTime(200L);
        //logRecordInfo.setCreateTime(new Date());
        logRecordRepository.save(logRecordInfo);
        return R.ok();
    }


    @GetMapping("/list2")
    public R list2(String keyword,String moduleName){
        Object search = searchRequestService.search(0, 10, keyword, moduleName);
        return R.ok(search);
    }


    @GetMapping("/list3")
    public R list3(String keyword,String moduleName){
        Object search = searchRequestService.search2(0, 10, keyword, moduleName);
        return R.ok(search);
    }

}
