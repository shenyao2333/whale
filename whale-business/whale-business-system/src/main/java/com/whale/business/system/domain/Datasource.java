package com.whale.business.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.whale.provider.basices.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_datasource")
public class Datasource extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 驱动名称
     */
    private String driverClassName;

    /**
     * url地址
     */
    private String url;

    /**
     * 用户
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 别名
     */
    private String alias;
}
