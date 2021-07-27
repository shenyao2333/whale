package com.whale.provider.es.constant;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @Author: sy
 * @Date: Created by 2021/7/27 0027-22:00
 * @description:
 */

public class LogInfoEs {

    /**
     * index 是否创建索引，默认创建
     */
    @Field(type = FieldType.Long)
    private Long id;


    @Field(analyzer = "ik_max_word",type = FieldType.Text,store = true,searchAnalyzer = "ik_max_word")
    private String value;


    /**
     * 请求的url
     */
    @Field(index = false,type = FieldType.Text)
    private String url;

    /**
     * 传入参数
     */
    @Field(index = false,type = FieldType.Text)
    private String  param;


    /**
     * analyzer 存入时是否使用分词
     * type 在es里的类型。不指定可自动
     * store 是否独立存储
     * searchAnalyzer 查询分词
     */
    @Field(analyzer = "ik_max_word",type = FieldType.Text,store = true,searchAnalyzer = "ik_max_word")
    private String className;

    /**
     * 方法名
     */
    @Field(analyzer = "ik_max_word",type = FieldType.Text,store = true,searchAnalyzer = "ik_max_word")
    private String methodName;

    /**
     * 错误信息
     */
    @Field(analyzer = "ik_max_word",type = FieldType.Text,store = true,searchAnalyzer = "ik_max_word")
    private String errorMsg;


    /**
     * 返回结果
     */
    @Field(analyzer = "ik_max_word",type = FieldType.Text,store = true,searchAnalyzer = "ik_max_word")
    private String returnResult;

    /**
     *  耗时
     */
    @Field(type = FieldType.Long,index = false)
    private Long elapsedTime;


}
