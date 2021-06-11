package com.whale.business.system.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.whale.provider.basices.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author entfrm
 * @date 2020-04-23 18:15:10
 * @description 应用对象 Application
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_application")
public class Application extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 应用名称
     */
    @ExcelProperty(value = "应用名称")
    private String name;

    /**
     * 应用类型
     */
    @ExcelProperty(value = "应用类型")
    private String type;

    /**
     * 行业
     */
    @ExcelProperty(value = "行业")
    private String industry;

    /**
     * 封面
     */
    private String cover;

    /**
     * 版本
     */
    @ExcelProperty(value = "版本")
    private String version;

    /**
     * 描述
     */
    private String description;

    /**
     * 机构编号
     */
    private Integer deptId;

    /**
     * 状态
     */
    @ExcelProperty(value= "状态")
    private String status;


}
