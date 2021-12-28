package com.whale.api.order.feign.fallback;

import com.whale.api.order.domain.TestOrderInfo;
import com.whale.api.order.feign.TestOrderFeign;

import com.whale.provider.basices.web.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author sy
 * @date: 2021/3/9 16:15
 * @description
 */
@Slf4j
@Component
public class TestOrderFeignFallback implements TestOrderFeign {


    @Override
    public R<TestOrderInfo> test() {
        log.error("调用feign失败----");
        return R.timeout();
    }
}
