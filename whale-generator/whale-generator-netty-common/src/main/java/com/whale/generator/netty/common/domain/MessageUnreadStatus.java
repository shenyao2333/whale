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
 * @Date: Created by 2020/12/30 21:17
 * @description: ${description}
 */
@ApiModel(value="com-whale-generator-netty-common-domain-MessageUnreadStatus")
@Data
@TableName(value = "message_unread_status")
public class MessageUnreadStatus implements Serializable {
    @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty(value="")
    private Integer id;

    @TableField(value = "msg_id")
    @ApiModelProperty(value="")
    private Long msgId;

    @TableField(value = "cmsg_id")
    @ApiModelProperty(value="")
    private String cmsgId;

    /**
     * 创建时间
     */
    @TableField(value = "created")
    @ApiModelProperty(value="创建时间")
    private Date created;

    /**
     * 发送人
     */
    @TableField(value = "send_user_id")
    @ApiModelProperty(value="发送人")
    private Integer sendUserId;

    /**
     * 接收人
     */
    @TableField(value = "accepter_id")
    @ApiModelProperty(value="接收人")
    private Integer accepterId;

    /**
     * 消息 状态
     */
    @TableField(value = "msg_staus")
    @ApiModelProperty(value="消息 状态")
    private String msgStaus;

    private static final long serialVersionUID = 1L;
}