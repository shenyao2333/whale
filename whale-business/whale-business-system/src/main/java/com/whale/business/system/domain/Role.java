package com.whale.business.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.whale.provider.basices.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_role")
public class Role extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色权限字符串
     */
    private String code;

    /**
     * 所属应用
     */
    private String applications;

    /**
     * 数据范围（1：全部数据权限 2：自定数据权限）
     */
    private String dataScope;

    /**
     * 显示顺序
     */
    private Integer sort;

    /**
     * 角色状态（0正常 1停用）
     */
    private String status;

    /** 用户是否存在此角色标识 默认不存在 */
    @TableField(exist = false)
    private boolean flag = false;

    @TableField(exist = false)
    private List<Role> roles;

    /** 角色组 */
    @TableField(exist = false)
    private Long[] roleIds;

    /** 菜单组 */
    @TableField(exist = false)
    private Integer[] menuIds;

    /** 机构（数据权限） */
    @TableField(exist = false)
    private Integer[] deptIds;

}
