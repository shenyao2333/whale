package com.whale.business.system.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_login_log")
public class LoginLog {

    private static final long serialVersionUID = 1L;

    /**
     * 访问ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 登录账号
     */
    @ExcelProperty(value = "登录账号")
    private String loginName;

    /**
     * 登录类型
     */
    @ExcelProperty(value = "登录类型 0=登录,1=退出")
    private String loginType;

    /**
     * 登录IP地址
     */
    @ExcelProperty(value = "登录IP地址")
    private String loginIp;

    /**
     * 登录地点
     */
    @ExcelProperty(value = "登录地点")
    private String loginAddr;

    /**
     * 用户代理
     */
    @ExcelProperty(value = "用户代理")
    private String userAgent;

    /**
     * 登录状态（0成功 1失败）
     */
    @ExcelProperty(value = "登录状态 0=成功,1=失败")
    private String status;

    /**
     * 提示消息
     */
    private String msg;

    /**
     * 访问时间
     */
    @ExcelProperty(value = "访问时间", format = "yyyy-MM-dd HH:mm:ss")
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
