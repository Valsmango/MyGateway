package com.ymt.netty01;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;


/**
 * 6, 自定义一个Handler，继承Netty规定好的适配器（还有许多其他适配器）
 */
public class BasicTCPserverHandler extends ChannelInboundHandlerAdapter {
    // 读取客户端发送的消息，可以将消息传到关联的pipeline中
    // ChannelHandlerContext ctx：上下文对象，可以得到pipeline，通道，地址
    // Object msg：客户端的消息
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("服务器读取线程" + Thread.currentThread().getName());
        System.out.println("server ctx = " + ctx);
        ByteBuf buf = ((ByteBuf) msg);  // 注意这个ByteBuf和NIO原生ByteBuffer不同
        System.out.println("客户端发送信息是：" + buf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址为：" + ctx.channel().remoteAddress());
    }

    // 读取完客户端发送的数据，将信息发回去
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 将输入写入到缓存并flush
        ctx.writeAndFlush(Unpooled.copiedBuffer("是滴是滴是滴！！！！", CharsetUtil.UTF_8));
    }

    // 异常处理，一般是需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
