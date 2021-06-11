package com.whale.business.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.whale.provider.basices.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;



@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_backup")
public class Backup extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 编号 */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 名称 */
    private String name;

    /** 路径 */
    private String path;

}
