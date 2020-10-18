package com.whale.generator.netty.test;

import com.whale.generator.netty.proto.Userinfo;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author sy
 * @date: 2020/10/16 16:28
 * @description
 */
public class Test2 {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSock = new ServerSocket(3030);
        Socket sock = serverSock.accept();
        byte[] msg = new byte[16];

        sock.getInputStream().read(msg);


        Userinfo.UserInfo userInfo = Userinfo.UserInfo.parseFrom(msg);
        System.out.println("消息--》"+userInfo.toString());

    }


}
