package com.whale.business.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.whale.provider.basices.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 参数配置表
 * </p>
 *
 * @author entfrm
 * @since 2019-01-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_config")
public class Config extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 参数主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 参数名称
     */
    private String name;

    /**
     * 参数键名
     */
    @TableField("`key`")
    private String key;

    /**
     * 参数键值
     */
    @TableField("`value`")
    private String value;

    /**
     * 系统内置
     */
    private String isSys;

}
