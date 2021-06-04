package com.whale.business.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.entfrm.base.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 系统访问记录
 * </p>
 *
 * @author entfrm
 * @since 2019-01-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_login_log")
public class LoginLog {

    private static final long serialVersionUID = 1L;

    /**
     * 访问ID
     */
    @TableId
    private Integer id;

    /**
     * 登录账号
     */
    @Excel(name = "登录账号")
    private String loginName;

    /**
     * 登录类型
     */
    @Excel(name = "登录类型", convertExp = "0=登录,1=退出")
    private String loginType;

    /**
     * 登录IP地址
     */
    @Excel(name = "登录IP地址")
    private String loginIp;

    /**
     * 登录地点
     */
    @Excel(name = "登录地点")
    private String loginAddr;

    /**
     * 用户代理
     */
    @Excel(name = "用户代理")
    private String userAgent;

    /**
     * 登录状态（0成功 1失败）
     */
    @Excel(name = "登录状态", convertExp = "0=成功,1=失败")
    private String status;

    /**
     * 提示消息
     */
    private String msg;

    /**
     * 访问时间
     */
    @Excel(name = "访问时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date loginTime;

    /** 开始时间 */
    @TableField(exist = false)
    @JsonIgnore
    private String beginTime;

    /** 结束时间 */
    @TableField(exist = false)
    @JsonIgnore
    private String endTime;
}
