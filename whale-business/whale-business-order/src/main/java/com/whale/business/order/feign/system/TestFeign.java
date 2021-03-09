package com.whale.business.order.feign.system;

import com.whale.business.order.feign.system.fallback.TestFeignFallback;
import com.whale.provider.basices.web.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author sy
 * @date: 2021/3/8 17:04
 * @description
 */
@Service
@FeignClient(value ="whale-business-system", fallback = TestFeignFallback.class)
public interface TestFeign {


    @GetMapping("/sys/secretKey/test")
    R<Object> getTest();


}
