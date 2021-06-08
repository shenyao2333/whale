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
        Object createTime = getFieldValByName("createTime", metaObject);
        if (createTime == null ) {
            setFieldValByName("createTime", new Date(), metaObject);
        }

        Object createBy = getFieldValByName("createBy", metaObject);
        if (createBy == null) {
            setFieldValByName("createBy", getUserName(), metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Object fieldValue = getFieldValByName("updateTime", metaObject);
        if (fieldValue == null) {
            setFieldValByName("updateTime", new Date(), metaObject);
        }
        Object updateBy = getFieldValByName("updateBy", metaObject);
        if (updateBy == null) {
            setFieldValByName("updateBy", getUserName(), metaObject);
        }
    }


    protected Object getUserName() {
        return "";
    }


}
