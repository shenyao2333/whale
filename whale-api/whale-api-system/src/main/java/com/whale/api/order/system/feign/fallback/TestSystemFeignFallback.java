package com.whale.api.order.system.feign.fallback;

import com.whale.api.order.system.feign.TestSystemFeign;
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
public class TestSystemFeignFallback implements TestSystemFeign {


    @Override
    public R<Object> getTest() {
        log.error("调用feign失败----");
        return null;
    }


}
