package com.whale.business.order.service.impl;


import com.whale.provider.basices.web.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author sy
 * @date: 2021/3/11 13:45
 * @description
 */
@Service
@FeignClient(value ="whale-business-system")
public interface Test2Feign {

    @GetMapping("/sys/secretKey/test")
    R<Object> getTest();


}


