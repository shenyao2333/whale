package com.whale.generator.netty.test;

import com.whale.generator.netty.proto.Mobile;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author sy
 * @date: 2020/10/16 16:11
 * @description
 */
public class Server {


    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        ServerSocket serverSock = new ServerSocket(3030);
        Socket sock = serverSock.accept();
        byte[] msg = new byte[256];
        sock.getInputStream().read(msg);
        String str = new String(msg, "UTF-8");

        System.out.println("---->");
        System.out.println("收到:"+str);

        Thread.sleep(1100);

    }

}
