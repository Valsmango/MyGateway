package com.ymt.netty04;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;


public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {
    // 定义一个channel组，管理所有的channel
    // 为什么可以得到所有的channel呢？
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // handlerAdded表示连接建立，一旦连接，第一个被执行
    // 将当前的channel加入到channelGroup
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // 将该用户加入聊天的信息推送给其他在线客户端
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[" + sdf.format(new Date()) + " 客户端：" + channel.remoteAddress() + "]加入聊天\n");
        channelGroup.add(channel);
    }

    // 表示channel处于活动状态，提示xxx上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        channelGroup.writeAndFlush("[" + sdf.format(new Date()) + " 客户端：" + ctx.channel().remoteAddress() + "]已上线\n");
    }

    // 表示channel处于不活动状态，提示xxx下线
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        channelGroup.writeAndFlush("[" + sdf.format(new Date()) + " 客户端" + ctx.channel().remoteAddress() + "]已下线\n");
    }

    // 表示断开连接
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        channelGroup.writeAndFlush("[" + sdf.format(new Date()) + " 客户端" + ctx.channel().remoteAddress() + "]已离开聊天\n");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
        Channel channel = channelHandlerContext.channel();
        channelGroup.forEach(ch -> {
            if (channel != ch) {    // 不是当前的channel，则直接转发
                ch.writeAndFlush("[" + sdf.format(new Date()) + " 客户端" + channel.remoteAddress() + "]发送了消息：" + msg + "\n");
            } else {
                ch.writeAndFlush("[自己]已经发送消息：" + msg + "\n");
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
