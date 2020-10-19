package com.whale.generator.netty.test;

import com.whale.generator.netty.proto.Mobile;

import java.io.IOException;
import java.net.Socket;

/**
 * @author sy
 * @date: 2020/10/16 16:08
 * @description
 */
public class Client {

    public static void main(String[] args) throws IOException {


        Socket socket = new Socket("127.0.0.1",3030);
        Mobile.MobilePhone.Builder builder = Mobile.MobilePhone.newBuilder();
        builder.setBrand("asdfasdf").setHardware("123123");
        byte[] messageBody = builder.build().toByteArray();
        System.out.println(messageBody.length);

        socket.getOutputStream().write(messageBody);


    }


}
