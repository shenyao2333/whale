package com.xxl.job.executor.service.jobhandler;

import com.whale.api.order.domain.vo.OrderInfoVo;
import com.whale.api.order.dubbo.service.TestDubboService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Author: sy
 * @Date: Created by 2021/11/26 16:09
 * @description:
 */
@Component
public class TestJob  {


    @Reference
    private TestDubboService testDubboService;
    /**
     * 1、简单任务示例（Bean模式）
     */
    @XxlJob("testOrder")
    public void demoJobHandler() throws Exception {
        System.out.println("开始调用");
        XxlJobHelper.log("开始调用order了.");
        try {
            OrderInfoVo orderInfoVo = testDubboService.getOrderInfoVo(123);
            XxlJobHelper.log("开始成功--》"+orderInfoVo);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            throw e;
        }

        XxlJobHelper.handleSuccess();
    }
}
