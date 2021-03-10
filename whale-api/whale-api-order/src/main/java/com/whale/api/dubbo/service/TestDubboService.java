package com.whale.api.dubbo.service;

import com.whale.api.dubbo.domain.vo.OrderInfoVo;

/**
 * @author sy
 * @date: 2021/3/10 11:25
 * @description
 */
public interface TestDubboService {


    OrderInfoVo getOrderInfoVo(Integer orderId);

}
