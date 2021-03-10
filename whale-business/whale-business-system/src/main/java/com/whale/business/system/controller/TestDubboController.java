package com.whale.business.system.controller;

import com.whale.provider.basices.web.R;
import com.whale.api.dubbo.domain.vo.OrderInfoVo;
import com.whale.api.dubbo.service.TestDubboService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sy
 * @date: 2021/3/10 11:43
 * @description
 */
@RestController
@RequestMapping("/dubbo")
@RequiredArgsConstructor
@Api(tags = "测试-RPC调用测试")
public class TestDubboController {

    @Reference
    private TestDubboService testDubboService;

    @GetMapping("/getOrderInfo")
    public R getOrderInfo(Integer orderId){
        OrderInfoVo orderInfoVo = testDubboService.getOrderInfoVo(orderId);
        return R.ok(orderInfoVo);
    }





}
