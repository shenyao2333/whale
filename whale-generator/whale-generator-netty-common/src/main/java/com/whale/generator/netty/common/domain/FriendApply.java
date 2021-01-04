package com.whale.generator.netty.common.domain;

import com.baomidou.mybatisplus.annotation.*;
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
 * @Date: Created by 2021/1/3 21:11
 * @description: ${description}
 */
/**
    * 好友申请表
    */
@ApiModel(value="com-whale-generator-netty-common-domain-FriendApply")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "friend_apply")
public class FriendApply implements Serializable {
    @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty(value="")
    private Long id;

    /**
     * 接收人id
     */
    @TableField(value = "accepter_user_id")
    @ApiModelProperty(value="接收人id")
    private Long accepterUserId;

    /**
     * 申请人id
     */
    @TableField(value = "apply_user_id")
    @ApiModelProperty(value="申请人id")
    private Long applyUserId;

    /**
     * 创建时间
     */
    @TableField(value = "created")
    @ApiModelProperty(value="创建时间")
    private Date created;

    /**
     * 更新时间
     */
    @TableField(value = "updated" )
    @ApiModelProperty(value="更新时间" )
    private Date updated;

    /**
     * 申请状态。1-等待回复，2-拒绝，3-同意
     */
    @TableField(value = "apply_status")
    @ApiModelProperty(value="申请状态。1-等待回复，2-拒绝，3-同意")
    private String applyStatus;

    /**
     * 申请备注
     */
    @TableField(value = "apply_content")
    @ApiModelProperty(value="申请备注")
    private String applyContent;

    /**
     * 拒绝备注
     */
    @TableField(value = "reject_content")
    @ApiModelProperty(value="拒绝备注")
    private String rejectContent;

    private static final long serialVersionUID = 1L;
}