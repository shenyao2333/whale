package com.whale.generator.netty.client.config;

import com.whale.generator.netty.common.protocol.Msg;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author pjmike
 * @create 2018-10-24 16:31
 */
@Component
@Slf4j
public class NettyClient {

    @Value("${netty.port}")
    private int port;
    @Value("${netty.host}")
    private String host;

    private final EventLoopGroup group = new NioEventLoopGroup();

    private SocketChannel socketChannel;

    @Resource
    private  ClientHandlerInitilizer clientHandlerInitilizer;

    public void sendMsg(Msg.Base message) {
        socketChannel.writeAndFlush(message);
    }

    @PostConstruct
    public void start()  {
        log.info("又进来了");
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .remoteAddress(host, port)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(clientHandlerInitilizer);
        ChannelFuture future = bootstrap.connect();
        //客户端断线重连逻辑
        log.info("到这");
        future.addListener((ChannelFutureListener) f -> {
            if (f.isSuccess()) {
                log.info("连接Netty服务端成功");
            } else {
                log.info("连接失败，进行断线重连");
                f.channel().eventLoop().schedule(this::start, 30,TimeUnit.SECONDS);
            }
        });
        socketChannel = (SocketChannel) future.channel();
    }

    public  void doConnect() {
       //log.info("进行重连中");
       // Bootstrap bootstrap = new Bootstrap();
       // bootstrap.group(group)
       //         .channel(NioSocketChannel.class)
       //         .remoteAddress(host, port)
       //         .option(ChannelOption.SO_KEEPALIVE, true)
       //         .option(ChannelOption.TCP_NODELAY, true)
       //         // 设置TCP的长连接，默认的 keepalive的心跳时间是两个小时
       //         // .childOption(ChannelOption.SO_KEEPALIVE, true)
       //         .handler(new ClientHandlerInitilizer());
       // bootstrap.connect();
    }
}
