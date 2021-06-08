package com.whale.provider.security.handler;

import com.whale.provider.basices.handler.BaseMetaObjectHandler;
import com.whale.provider.security.utils.SecurityUtil;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * @Author: sy
 * @Date: Created by 2021/6/8 10:01
 * @description:
 */
@Primary
@Component
public class AuthMetaObjectHandler extends BaseMetaObjectHandler {

    @Override
    protected Object getUserName() {
        try {
            return SecurityUtil.getUser().getUsername();
        } catch (Exception e) {
            return "";
        }
    }




}
