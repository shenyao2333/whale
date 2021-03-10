package com.whale.business.order.controller;

import com.whale.business.order.feign.system.TestFeign;
import com.whale.provider.basices.web.R;
import com.whale.api.dubbo.domain.vo.OrderInfoVo;
import com.whale.api.dubbo.service.TestDubboService;
import lombok.RequiredArgsConstructor;
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

    private final TestFeign testFeign;

   private final TestDubboService testDubboService;

    @GetMapping("/test")
    public R test1(){
        return R.ok("订单服务！");
    }


    @GetMapping("/testFei")
    public R testFei(){
        R<Object> test = testFeign.getTest();
        if (test==null){
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
