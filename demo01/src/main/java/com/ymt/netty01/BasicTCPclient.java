package com.ymt.netty01;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class BasicTCPclient {
    public static void main(String[] args) throws InterruptedException {
        // 客户端只需要一个事件循环组（实质就是一个线程组）
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();

        try {
            // 创建客户端的启动对象
            Bootstrap bootstrap = new Bootstrap();

            // 设置相关参数
            bootstrap.group(eventExecutors) // 将循环组交给启动对象
                    .channel(NioSocketChannel.class)    // 方便框架通过反射创建channel
                    .handler(new ChannelInitializer<SocketChannel>() {  // 设置channel对应的handler
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new BasicTCPclientHandler()); // 将handler扔进pipeline中，pipeline相当于比channel更厉害的一个东西
                        }
                    });
            System.out.println("—————————————————— Bootstrap-Client is READY！----------------");

            // 激活启动器，将服务器端的消息包装成一个ChannelFuture对象（虽然调用的是sync方法，但是ChannelFuture是异步的）
            ChannelFuture channelFuture = bootstrap.connect("localhost", 6668).sync();
//            ChannelFuture channelFuture = bootstrap.connect("localhost", 8888).sync();
            // 进行关闭监听
            channelFuture.channel().closeFuture().sync();
        } finally {    // 无catch是因为抛出了异常
            eventExecutors.shutdownGracefully();
        }
    }
}
