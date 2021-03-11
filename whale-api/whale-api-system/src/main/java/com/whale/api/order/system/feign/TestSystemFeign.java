package com.whale.api.order.system.feign;

import com.whale.api.order.system.feign.fallback.TestSystemFeignFallback;
import com.whale.provider.basices.web.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author sy
 * @date: 2021/3/11 11:53
 * @description
 */
@Service
@FeignClient(value ="whale-business-system", fallback = TestSystemFeignFallback.class)
public interface TestSystemFeign {


    @GetMapping("/sys/secretKey/test")
    R<Object> getTest();


}
