package com.whale.generator.netty.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.whale.generator.netty.proto.TestJson;
import com.whale.generator.netty.proto.Userinfo;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author sy
 * @date: 2020/10/16 16:28
 * @description
 */
public class Test  {

    public static void main(String[] args) throws IOException, InterruptedException {
        Socket socket = new Socket("127.0.0.1",3030);

        Userinfo.UserInfo.Builder builder = Userinfo.UserInfo.newBuilder();
        builder.setName("123").setPassword("123456").setSex("1").build();
        byte[] msgBody = builder.build().toByteArray();
        System.out.println(msgBody.length);


        socket.getOutputStream().write(msgBody);
        Thread.sleep(1000);


    }



}
