package com.whale.generator.netty.common.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: shenyao
 * @Date: Created by 2021/1/4 21:14
 * @description: ${description}
 */
@ApiModel(value="com-whale-generator-netty-common-domain-Friend")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "friend")
public class Friend implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="")
    private Integer id;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    @ApiModelProperty(value="用户id")
    private Integer userId;

    /**
     * 好友id
     */
    @TableField(value = "friend_user_id")
    @ApiModelProperty(value="好友id")
    private Integer friendUserId;

    /**
     * 0-正常，1-删除
     */
    @TableField(value = "del")
    @ApiModelProperty(value="0-正常，1-删除")
    private String del;

    /**
     * 分组id
     */
    @TableField(value = "group_id")
    @ApiModelProperty(value="分组id")
    private Integer groupId;

    /**
     * 更新时间
     */
    @TableField(value = "updated")
    @ApiModelProperty(value="更新时间")
    private Date updated;

    /**
     * 创建时间
     */
    @TableField(value = "created")
    @ApiModelProperty(value="创建时间")
    private Date created;

    /**
     * 备注
     */
    @TableField(value = "remark")
    @ApiModelProperty(value="备注")
    private String remark;

    private static final long serialVersionUID = 1L;
}