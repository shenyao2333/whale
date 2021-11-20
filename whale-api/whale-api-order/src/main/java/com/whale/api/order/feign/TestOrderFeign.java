package com.whale.api.order.feign;

import com.whale.api.order.domain.TestOrderInfo;
import com.whale.api.order.feign.fallback.TestOrderFeignFallback;
import com.whale.provider.basices.web.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author sy
 * @date: 2021/3/10 17:13
 * @description
 */
@Service
@FeignClient(value ="whale-business-order", fallback = TestOrderFeignFallback.class)
public interface TestOrderFeign {


    @GetMapping(value = "/test/testDubbo" )
    R<TestOrderInfo> test();
}
