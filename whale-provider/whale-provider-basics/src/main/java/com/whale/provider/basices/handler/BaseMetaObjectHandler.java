package com.whale.provider.basices.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author: shenyao
 * @Date: Created by 2021/1/4 21:42
 * @description:
 */
@Component
public class BaseMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        Object createTime = getFieldValByName("created", metaObject);
        if (createTime == null ) {
            setFieldValByName("created", new Date(), metaObject);
        }

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Object fieldValue = getFieldValByName("created", metaObject);
        if (fieldValue == null) {
            setFieldValByName("created", new Date(), metaObject);
        }

    }
}
