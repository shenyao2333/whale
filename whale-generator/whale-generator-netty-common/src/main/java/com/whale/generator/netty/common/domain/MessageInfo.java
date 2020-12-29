package com.whale.generator.netty.common.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @Author: shenyao
 * @Date: Created by 2020/11/29 1:28
 * @description: ${description}
 */

/**
 * 消息 消息表
 */
@ApiModel(value = "MessageInfo")
@Data
@TableName(value = "message_info")
public class MessageInfo implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 发送人
     */
    @TableField(value = "send_user_id")
    @ApiModelProperty(value = "发送人")
    private Integer sendUserId;

    /**
     * 接收人
     */
    @TableField(value = "accepter_id")
    @ApiModelProperty(value = "接收人")
    private Integer accepterId;

    /**
     * 消息状态 0-未读，1-已读，2-撤回，3-删除
     */
    @TableField(value = "msg_status")
    @ApiModelProperty(value = "消息状态 0-未读，1-已读，2-撤回，3-删除")
    private String msgStatus;

    /**
     * 发送类型 0-私聊，1-群发信息
     */
    @TableField(value = "send_type")
    @ApiModelProperty(value = "发送类型 0-私聊，1-群发信息")
    private String sendType;

    /**
     * 消息内容
     */
    @TableField(value = "content")
    @ApiModelProperty(value = "消息内容")
    private String content;

    /**
     * 创建时间
     */
    @TableField(value = "created")
    @ApiModelProperty(value = "创建时间")
    private Date created;

    /**
     * 更新时间
     */
    @TableField(value = "updated")
    @ApiModelProperty(value = "更新时间")
    private Date updated;

    private static final long serialVersionUID = 1L;
}