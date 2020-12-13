package com.whale.generator.netty.client.config;
import com.whale.generator.netty.common.protocol.MsgBase;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author sy
 * @date: 2020/10/19 9:55
 * @description 客户端处理器初始化
 */
public class ClientHandlerInitilizer  extends ChannelInitializer<Channel> {
    @Override
    protected void initChannel(Channel channel) throws Exception {
        channel.pipeline()
                 //心跳设置   参数：未发生读，未发生写 ，未发生读写 。 0代表不处理。
                .addLast(new IdleStateHandler(0, 10, 0))
                //根据消息长度字段值动态地分割所接收到的 ByteBuf
                .addLast(new ProtobufVarint32FrameDecoder())
                // protobuf解码
                .addLast(new ProtobufDecoder(MsgBase.Msg.getDefaultInstance()))
                //给消息对象标注长度
                .addLast(new ProtobufVarint32LengthFieldPrepender())
                //解码
                .addLast(new ProtobufEncoder())
                //心跳消息发送
                .addLast(new HeartbeatHandler())
                //消息接收处理
                .addLast(new NettyClientHandler());
    }
}
