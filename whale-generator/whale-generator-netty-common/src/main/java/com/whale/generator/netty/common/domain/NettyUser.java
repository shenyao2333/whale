package com.whale.generator.netty.common.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_user")
public class NettyUser {
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    /**
     * 使用名
     */
    @TableField(value = "user_name")
    private String userName;

    /**
     * 真实姓名
     */
    @TableField(value = "real_name")
    private String realName;

    /**
     * QQ号
     */
    @TableField(value = "qq")
    private String qq;

    /**
     * 微信号
     */
    @TableField(value = "we_chat")
    private String weChat;

    /**
     * 密码
     */
    @TableField(value = "`password`")
    private String password;

    /**
     * 手机号
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 0-女，1-男
     */
    @TableField(value = "sex")
    private Integer sex;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 头像
     */
    @TableField(value = "avatar")
    private String avatar;

    /**
     * 用户状态，0-正常，1-停用
     */
    @TableField(value = "`status`")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(value = "created")
    private Date created;

    /**
     * 更新时间
     */
    @TableField(value = "updated")
    private Date updated;

    /**
     * 省
     */
    @TableField(value = "province")
    private String province;

    /**
     * 市
     */
    @TableField(value = "city")
    private String city;

    /**
     * 县
     */
    @TableField(value = "county")
    private String county;

    /**
     * 详细地址
     */
    @TableField(value = "address")
    private String address;

    /**
     * 用户类型 0-普通用户，1-后台用户
     */
    @TableField(value = "`type`")
    private Integer type;

    /**
     * 0-正常，1-删除
     */
    @TableField(value = "del")
    private String del;

    public static final String COL_ID = "id";

    public static final String COL_USER_NAME = "user_name";

    public static final String COL_REAL_NAME = "real_name";

    public static final String COL_QQ = "qq";

    public static final String COL_WE_CHAT = "we_chat";

    public static final String COL_PASSWORD = "password";

    public static final String COL_PHONE = "phone";

    public static final String COL_SEX = "sex";

    public static final String COL_EMAIL = "email";

    public static final String COL_AVATAR = "avatar";

    public static final String COL_STATUS = "status";

    public static final String COL_CREATED = "created";

    public static final String COL_UPDATED = "updated";

    public static final String COL_PROVINCE = "province";

    public static final String COL_CITY = "city";

    public static final String COL_COUNTY = "county";

    public static final String COL_ADDRESS = "address";

    public static final String COL_TYPE = "type";

    public static final String COL_DEL = "del";
}