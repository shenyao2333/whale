package com.whale.business.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.entfrm.base.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 操作日志记录
 * </p>
 *
 * @author entfrm
 * @since 2019-01-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_oper_log")
public class OperLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志主键
     */
    @TableId
    private Integer id;

    /**
     * 模块标题
     */
    @Excel(name = "标题")
    private String title;

    /**
     * 业务类型（0其它 1新增 2修改 3删除）
     */
    private Integer type;

    /**
     * 方法名称
     */
    @Excel(name = "方法名称")
    private String method;

    /**
     * 用户代理
     */
    @Excel(name = "用户代理")
    private String userAgent;

    /**
     * 操作人员
     */
    @Excel(name = "操作人员")
    private String operName;

    /**
     * 终端编号
     */
    @Excel(name = "客户端")
    private String clientId;

    /**
     * 请求URL
     */
    @Excel(name = "请求URL")
    private String operUrl;

    /**
     * 主机地址
     */
    @Excel(name = "主机地址")
    private String operIp;

    /**
     * 操作地点
     */
    @Excel(name = "操作地点")
    private String operAddr;

    /**
     * 请求参数
     */
    private String operParam;

    /**
     * 操作状态（0正常 1异常）
     */
    @Excel(name = "操作状态", convertExp = "0=正常,1=异常")
    private Integer status;

    /**
     * 错误消息
     */
    private String errorMsg;

    /**
     * 错误消息
     */
    private String executeTime;

    /**
     * 操作时间
     */
    @Excel(name = "操作时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date operTime;

    /** 开始时间 */
    @TableField(exist = false)
    @JsonIgnore
    private String beginTime;

    /** 结束时间 */
    @TableField(exist = false)
    @JsonIgnore
    private String endTime;
}
