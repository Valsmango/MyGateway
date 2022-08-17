package com.ymt.netty02;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class MyHttpChannelInitializer extends ChannelInitializer<SocketChannel>{
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        // 为什么要在这里进行HTTP协议的编码解码？
        // 因为bossGroup/Boss的Selector只负责监听连接请求，也就是事件是否为Accept；
        // 所有连接后的业务逻辑都需要在workerGroup中处理，
        // workGroup中的每一个EventLoop对应一个Selector，一个Selector对应多个client的Channel（1个Channel对应1个pipeline，
        // 内部含一条链上的多个ChannelHandler，每个ChannelHandler需要通过一个ChannelHandlerContext获取相关信息完成业务）

        // 得到管道
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 加入HTTP的编码/解码器
        pipeline.addLast("MyHttpServerCodec", new HttpServerCodec());
        // 增加自定义的处理器
        pipeline.addLast("MyHttpServerHandler", new MyHttpServerHandler());
    }
}
