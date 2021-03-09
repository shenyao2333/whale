package com.whale.business.order.feign.system.fallback;

import com.whale.business.order.feign.system.TestFeign;
import com.whale.provider.basices.web.GrabException;
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
public class TestFeignFallback  implements TestFeign {


    @Override
    public R<Object> getTest() {
        log.error("调用feign失败----");
        return null;
    }


}
