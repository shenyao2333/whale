package com.whale.generator.netty.handler;

import com.alibaba.fastjson.JSON;
import com.whale.generator.netty.common.domain.NettyUser;
import com.whale.generator.netty.common.protocol.Cmd;
import com.whale.generator.netty.common.protocol.Msg;
import com.whale.generator.netty.common.service.NettyUserService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author sy
 * @date: 2021/1/22 13:48
 * @description 用户信息处理
 */
@Slf4j
@ChannelHandler.Sharable
@Component
@RequiredArgsConstructor
public class UserInfoServiceHandler extends ChannelInboundHandlerAdapter {


    private final NettyUserService userService;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object message) throws Exception {
        Msg.Base msg = (Msg.Base)message;
        if (msg.getCmd()== Cmd.Command.QUERY){
            String accepterIds = msg.getAccepterId();

            String[] split = accepterIds.split(",");
            new ArrayList<Integer>();
            List<Integer> userIds = Arrays.stream(split).map(Integer::parseInt).collect(Collectors.toList());
            List<NettyUser> userList =  userService.getByUserIds(userIds);
            String  userListJson =  JSON.toJSONString(userIds);

        }



    }

}
