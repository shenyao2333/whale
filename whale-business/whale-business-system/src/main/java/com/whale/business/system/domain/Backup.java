package com.whale.business.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.entfrm.data.entity.BaseEntity;

/**
 * @author entfrm
 * @date 2020-03-02 16:33:24
 *
 * @description 备份对象 Backup
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_backup")
public class Backup extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    @TableId
    private Integer id;

    /** 名称 */
    private String name;

    /** 路径 */
    private String path;

}
