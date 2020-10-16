package com.whale.generator.netty.test;

import com.whale.generator.netty.proto.Userinfo;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
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

        byte[] msg = new byte[256];

        System.out.println("---这里---");
        int read = sock.getInputStream().read(msg);

        String string = new String(msg, "UTF-8");
        System.out.println(string);

        Userinfo.UserInfo userInfo = Userinfo.UserInfo.parseFrom(msg);
        System.out.println("输出");
        System.out.println(userInfo);
        System.out.println(userInfo.getName());
        System.out.println(userInfo.toString());

    }


}
