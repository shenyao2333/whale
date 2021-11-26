package com.whale.business.order.controller;

import com.whale.api.order.feign.TestOrderFeign;
import com.whale.api.order.system.feign.TestSystemFeign;
import com.whale.business.order.service.impl.Test2Feign;
import com.whale.provider.basices.web.R;
import com.whale.api.order.domain.vo.OrderInfoVo;
import com.whale.api.order.dubbo.service.TestDubboService;
//import com.whale.provider.log.annotation.LogRecord;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: shenyao
 * @Date: Created by 2021/3/7 21:54
 * @description:
 */
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    @Reference
    private  TestDubboService testDubboService;

    private final TestSystemFeign systemFeign;
    private final Test2Feign test2Feign;

    @GetMapping("/test")
    //@LogRecord(value = "测试日志")
    public R test1(String name){
        return R.ok("订单服务！");
    }



    @GetMapping("/testFei")
    public R testFei(){
        R<Object> objectR = systemFeign.getTest();
        if (objectR==null){
            return R.fail("feign调用失败！");
        }
        return R.ok();
    }


    @GetMapping("/testFei2")
    public R testFei2(){
        R<Object> objectR = test2Feign.getTest();
        if (objectR==null){
            return R.fail("feign调用失败！");
        }
        return R.ok();
    }


   @GetMapping("/testDubbo")
   public R<OrderInfoVo> testDubbo(){
       OrderInfoVo orderInfoVo = testDubboService.getOrderInfoVo(3);
       return R.ok(orderInfoVo);
   }


}
