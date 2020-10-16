package com.whale.generator.netty.test;

import com.alibaba.fastjson.JSON;
import com.whale.generator.netty.proto.TestJson;
import com.whale.generator.netty.proto.Userinfo;

/**
 * @author sy
 * @date: 2020/10/16 16:47
 * @description
 */
public class TestSize  {

    public static void main(String[] args) {


        int p=0;
        long l = System.currentTimeMillis();
        for (int i=0;i<100;i++){
            Userinfo.UserInfo.Builder builder = Userinfo.UserInfo.newBuilder();
            builder.setSex("男").setPassword("123456").setName("沈瑶");
            byte[] bytes = builder.build().toByteArray();
            p+=bytes.length;
        }
        System.out.println("转化共花费时间："+(System.currentTimeMillis()-l));
        System.out.println("总计大小"+p);

        int jj =0;
        long l2 = System.currentTimeMillis();
        for (int j=0;j<100;j++){
            TestJson testJson = new TestJson();
            testJson.setName("沈瑶");
            testJson.setPassword("123456");
            testJson.setSex("男");
            byte[] bytes = JSON.toJSONString(testJson).getBytes();
            jj+=bytes.length;
        }
        System.out.println("转化共花费时间："+(System.currentTimeMillis()-l2));
        System.out.println("总计大小"+jj);


    }
}
