package com.whale.oauth2.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@ApiModel(value="com-whale-oauth2-domain-SysUser")
@Data
@TableName(value = "sys_user")
public class SysUser implements Serializable {


    @TableId(value = "user_id", type = IdType.AUTO)
    @ApiModelProperty(value="")
    private Integer userId;

    /**
     * 使用名
     */
    @TableField(value = "user_name")
    @ApiModelProperty(value="使用名")
    private String userName;

    /**
     * 真实姓名
     */
    @TableField(value = "real_name")
    @ApiModelProperty(value="真实姓名")
    private String realName;

    /**
     * QQ号
     */
    @TableField(value = "qq")
    @ApiModelProperty(value="QQ号")
    private String qq;

    /**
     * 微信号
     */
    @TableField(value = "we_chat")
    @ApiModelProperty(value="微信号")
    private String weChat;

    /**
     * 密码
     */
    @TableField(value = "password")
    @ApiModelProperty(value="密码")
    private String password;

    /**
     * 手机号
     */
    @TableField(value = "phone")
    @ApiModelProperty(value="手机号")
    private String phone;

    /**
     * 0-女，1-男
     */
    @TableField(value = "sex")
    @ApiModelProperty(value="0-女，1-男")
    private Integer sex;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    @ApiModelProperty(value="邮箱")
    private String email;

    /**
     * 头像
     */
    @TableField(value = "avatar")
    @ApiModelProperty(value="头像")
    private String avatar;

    /**
     * 用户状态，0-正常，1-停用
     */
    @TableField(value = "status")
    @ApiModelProperty(value="用户状态，0-正常，1-停用")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(value = "created")
    @ApiModelProperty(value="创建时间")
    private Date created;

    /**
     * 更新时间
     */
    @TableField(value = "updated")
    @ApiModelProperty(value="更新时间")
    private Date updated;

    /**
     * 省
     */
    @TableField(value = "province")
    @ApiModelProperty(value="省")
    private String province;

    /**
     * 市
     */
    @TableField(value = "city")
    @ApiModelProperty(value="市")
    private String city;

    /**
     * 县
     */
    @TableField(value = "county")
    @ApiModelProperty(value="县")
    private String county;

    /**
     * 详细地址
     */
    @TableField(value = "address")
    @ApiModelProperty(value="详细地址")
    private String address;



    @TableField(value = "type")
    @ApiModelProperty(value="用户类型0-普通用户，1-后台用户")
    private Integer type;

    private static final long serialVersionUID = 1L;

}
