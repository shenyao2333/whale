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
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 模块标题
     */
    @ExcelProperty(value = "标题")
    private String title;

    /**
     * 业务类型（0其它 1新增 2修改 3删除）
     */
    private Integer type;

    /**
     * 方法名称
     */
    @ExcelProperty(value = "方法名称")
    private String method;

    /**
     * 用户代理
     */
    @ExcelProperty(value = "用户代理")
    private String userAgent;

    /**
     * 操作人员
     */
    @ExcelProperty(value= "操作人员")
    private String operName;

    /**
     * 终端编号
     */
    @ExcelProperty(value = "客户端")
    private String clientId;

    /**
     * 请求URL
     */
    @ExcelProperty(value = "请求URL")
    private String operUrl;

    /**
     * 主机地址
     */
    @ExcelProperty(value = "主机地址")
    private String operIp;

    /**
     * 操作地点
     */
    @ExcelProperty(value = "操作地点")
    private String operAddr;

    /**
     * 请求参数
     */
    private String operParam;

    /**
     * 操作状态（0正常 1异常）
     */
    @ExcelProperty(value = "操作状态 0=正常,1=异常")
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
    @ExcelProperty(value = "操作时间")
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
