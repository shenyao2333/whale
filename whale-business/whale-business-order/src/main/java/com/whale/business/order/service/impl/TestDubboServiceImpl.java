package com.whale.business.order.service.impl;

import com.whale.provider.security.domain.WhaleUser;
import com.whale.provider.security.utils.SecurityUtil;
import com.whale.api.dubbo.domain.vo.OrderInfoVo;
import com.whale.api.dubbo.service.TestDubboService;
import org.apache.dubbo.config.annotation.Service;

import java.util.UUID;

/**
 * @author sy
 * @date: 2021/3/10 11:40
 * @description
 */
@Service
public class TestDubboServiceImpl implements TestDubboService {



    @Override
    public OrderInfoVo getOrderInfoVo(Integer orderId) {
        System.out.println("----进入订单服务----");
        OrderInfoVo orderInfoVo = new OrderInfoVo();
        orderInfoVo.setOrderId(orderId);
        orderInfoVo.setOrderSn(UUID.randomUUID().toString());
        orderInfoVo.setStatus(0);


        WhaleUser user = SecurityUtil.getUser();
       // System.out.println(user.getRealName());


        return orderInfoVo;
    }
}
