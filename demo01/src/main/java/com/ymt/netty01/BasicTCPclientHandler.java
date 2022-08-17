package com.ymt.netty01;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class BasicTCPclientHandler extends ChannelInboundHandlerAdapter {
    // 当通道就绪就会触发
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client " + ctx);
        ctx.writeAndFlush(Unpooled.copiedBuffer("你一定会有Offer的！！！！", CharsetUtil.UTF_8));
    }

    // 读取服务端发送的消息，可以将消息传到关联的pipeline中
    // ChannelHandlerContext ctx：上下文对象，可以得到pipeline，通道，地址
    // Object msg：服务端的消息
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("client ctx = " + ctx);
        ByteBuf buf = ((ByteBuf) msg);  // 注意这个ByteBuf和NIO原生ByteBuffer不同
        System.out.println("服务端回复的信息是：" + buf.toString(CharsetUtil.UTF_8));
        System.out.println("服务端地址为：" + ctx.channel().remoteAddress());
    }
}
