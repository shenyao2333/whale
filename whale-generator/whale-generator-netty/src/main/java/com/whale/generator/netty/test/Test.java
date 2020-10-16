package com.whale.generator.netty.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.whale.generator.netty.proto.TestJson;
import com.whale.generator.netty.proto.Userinfo;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.net.Socket;

/**
 * @author sy
 * @date: 2020/10/16 16:28
 * @description
 */
public class Test  {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1",3030);

        Userinfo.UserInfo.Builder builder = Userinfo.UserInfo.newBuilder();
        byte[] sd = builder.setName("123").setPassword("123456").setSex("1").build().toByteArray();

        socket.getOutputStream().write(sd);


    }


}
