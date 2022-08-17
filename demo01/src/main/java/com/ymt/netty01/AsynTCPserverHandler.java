package com.ymt.netty01;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

public class AsynTCPserverHandler  extends ChannelInboundHandlerAdapter {
    // 读取客户端发送的消息，可以将消息传到关联的pipeline中
    // ChannelHandlerContext ctx：上下文对象，可以得到pipeline，通道，地址
    // Object msg：客户端的消息
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 使用eventLoop来执行任务
        // 让channelReadComplete内的操作，不会因为这里的操作而阻塞住（也就是channelRead和channelReadComplete不再是严格一先一后）
        // 而是生成一个任务，扔进taskQueue中
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("waiting...");
                    ctx.writeAndFlush(Unpooled.copiedBuffer("不会阻塞的！！！！", CharsetUtil.UTF_8));
                    ByteBuf buf = ((ByteBuf) msg);  // 注意这个ByteBuf和NIO原生ByteBuffer不同
                    System.out.println("客户端发送信息是：" + buf.toString(CharsetUtil.UTF_8));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("waiting...");
                    Thread.sleep(20*1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("同一个线程，还是得等前面一个任务完成", CharsetUtil.UTF_8));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        // ctx.channel().eventLoop().schedule创建定时任务，会被扔进scheduleTaskQueue中
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("don't wannna wait!!!");
                    Thread.sleep(10*1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("定时任务", CharsetUtil.UTF_8));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 1, TimeUnit.MILLISECONDS);

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
