package com.whale.api.order.dubbo.service;

import com.whale.api.order.domain.vo.OrderInfoVo;

/**
 * @author sy
 * @date: 2021/3/10 11:25
 * @description
 */
public interface TestDubboService {


    OrderInfoVo getOrderInfoVo(Integer orderId);

}
