package com.whale.api.order.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author sy
 * @date: 2021/3/10 11:31
 * @description
 */
@Data
public class OrderInfoVo implements Serializable {


    private static final long serialVersionUID = 2454868981038644517L;
    /**
     * 订单id
     */
    private Integer orderId;
    /**
     * 订单号
     */
    private String orderSn;
    /**
     * 订单状态
     */
    private Integer status;

}
