package com.whale.provider.es.constant;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * @Author: sy
 * @Date: Created by 2021/7/27 0027-22:00
 * @description:
 */
@Data
@Document(indexName = "log_record-2021-08", shards = 1,replicas = 0)
public class LogRecordInfo {

    /**
     * index 是否创建索引，默认创建
     */
    @Field(type = FieldType.Text)
    private String id;


    /**
     * 模块名
     */
    @Field(type = FieldType.Text,store = true)
    private String moduleName;

    /**
     * 传入的值
     */
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



    /**
     * 创建时间DateFormat
     */
    @Field(type = FieldType.Date,format = DateFormat.custom , pattern = "uuuu-MM-dd'T'HH:mm:ss.SSSX" )
    private Date createTime = new Date();


}
