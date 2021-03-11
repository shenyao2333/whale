package com.whale.api.order.feign;

import com.whale.provider.basices.web.R;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author sy
 * @date: 2021/3/10 17:13
 * @description
 */

public interface TestOrderFeign {


    @GetMapping(value = "/test",consumes ={MediaType.APPLICATION_FORM_URLENCODED_VALUE}, produces = {MediaType.ALL_VALUE} )
    R<Object> test();
}
