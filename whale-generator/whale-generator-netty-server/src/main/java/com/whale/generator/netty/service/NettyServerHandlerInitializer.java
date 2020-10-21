package com.whale.generator.netty.service;

import com.whale.generator.netty.common.protocol.Message;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**
 * @author sy
 * @date Created in 2020.10.18 16:58
 * @description 消息的编码和解码
 */
public class NettyServerHandlerInitializer extends ChannelInitializer<Channel> {


    @Override
    protected void initChannel(Channel channel) throws Exception {
        channel.pipeline()
                //空闲检测
                .addLast(new ServerIdleStateHandler())
                //根据消息中的 Google Protocol Buffers 的 “Base 128 Varint" 整型长度字段值动态地分割所接收到的 ByteBuf
                .addLast(new ProtobufVarint32FrameDecoder())
                //使用 protobuf 对消息进行解码
                .addLast(new ProtobufDecoder(Message.Msg.getDefaultInstance()))
                //向 ByteBuf 前追加一个Google Protocol Buffers 的 “Base 128 Varint" 整型长度字段值
                .addLast(new ProtobufVarint32LengthFieldPrepender())
                //使用 protobuf 对消息进行编码
                .addLast(new ProtobufEncoder())
                .addLast(new NettyServerHandler());
    }



}
