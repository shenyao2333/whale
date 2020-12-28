package com.whale.generator.netty.handler;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: shenyao
 * @Date: Created by 2020/11/1 17:49
 * @description: Channel 管理
 */
public class ChannelManage {


    private static final ConcurrentHashMap<String, Channel> channelMap = new ConcurrentHashMap<>();
    private  static AttributeKey<String> key = AttributeKey.valueOf("user");

    /**
     * 判断一个通道是否有用户在使用
     * 可做信息转发时判断该通道是否合法
     * @param channel
     * @return
     */
    public static boolean hasUser(Channel channel) {
        //netty移除了这个map的remove方法,这里的判断谨慎一点
        return (channel.hasAttr(key) || channel.attr(key).get() != null);
    }

    /**
     * 上线一个用户
     *
     * @param channel
     * @param userId
     */
    public static void online(Channel channel, String userId) {
        channelMap.put(userId, channel);
        channel.attr(key).set(userId);
    }
    
    public static String  getUserId(Channel channel){
        if (channel.hasAttr(key)){
            return channel.attr(key).get();
        }
        return null;

    }

    /**
     * 根据用户id获取该用户的通道
     *
     * @param userId
     * @return
     */
    public static Channel getChannelByUserId(String userId) {
        return channelMap.get(userId);
    }

    /**
     * 判断一个用户是否在线
     *
     * @param userId
     * @return
     */
    public static Boolean isLine(String userId) {
        return channelMap.containsKey(userId) && channelMap.get(userId) != null;
    }

    /**
     * 下线用户
     *
     * @param userId
     * @return
     */
    public static void undock(String userId) {
        channelMap.remove(userId);
    }


}
