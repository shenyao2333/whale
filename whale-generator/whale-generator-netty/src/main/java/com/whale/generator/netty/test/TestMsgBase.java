package com.whale.generator.netty.test;

import com.google.protobuf.Timestamp;
import com.google.protobuf.util.Timestamps;
import com.whale.generator.netty.protocol.protobuf.MsgBase;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author sy
 * @date Created in 2020.10.18 18:07
 * @description
 */
public class TestMsgBase  {


    public static void main(String[] args) {
        MsgBase.Msg.newBuilder().setSendTime(123123);


    }
}
