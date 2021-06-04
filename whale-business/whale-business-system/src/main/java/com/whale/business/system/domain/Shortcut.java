package com.whale.business.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.whale.provider.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author entfrm
 * @date 2019-08-25 22:56:58
 *
 * @description 快捷方式对象 Shortcut
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_shortcut")
public class Shortcut extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 编号 */
    @TableId
    private Integer id;

    /** 名称 */
    private String name;

    /** 区域 */
    private String region;

    /** 图标 */
    private String icon;

    /** 背景颜色 */
    private String bgColor;

    /** 路径 */
    private String path;

    /** 顺序 */
    private Integer sort;

}
