package com.example.demo.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class OuterServer implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        // 1, 创建 Boss Group 和 Worker Group(两个线程组)，后续在运行中都会进行循环
        // 默认的线程数量为：Math.max(1, SystemPropertyUtil.getInt("io.netty.eventLoopThreads", NettyRuntime.availableProcessors() * 2));
        // 本机为4核CPU8线程，因而默认 8*2 == 16
        EventLoopGroup bossGroup = new NioEventLoopGroup(1); // 只处理连接请求
        EventLoopGroup workerGroup = new NioEventLoopGroup(8);   // 负责IO和业务处理

        try {
            // 2, 创建服务器的启动对象，配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();

            // 3, 使用链式编程来进行设置
            // 给我们的workGroup和EventLoop对应的管道设置处理器
            bootstrap.group(bossGroup, workerGroup) // 设置两个线程组
                    .channel(NioServerSocketChannel.class) // 使用NioServerSocketChannel作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG, 128)  // 设置线程队列得到的连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)  // 设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 创建一个通道测试对象
                        // 给pipeline设置处理器
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast(new OuterServerHandler());    // 向管道最后增加一个处理器 ----》 转到Handler的创建
                        }
                    });
            System.out.println("—————————————————— Outer Server's Bootstrap is READY！----------------");

            // 4, 启动服务器，绑定一个端口并进行同步，生成了一个ChannelFuture对象
            ChannelFuture channelFuture = bootstrap.bind(8866).sync();
//            channelFuture.addListener(new ChannelFutureListener() {
//                @Override
//                public void operationComplete(ChannelFuture channelFuture) throws Exception {
//                    if (channelFuture.isSuccess()) {
//                        System.out.println("监听端口 8866 成功");
//                    } else {
//                        System.out.println("监听端口 8866 失败");
//                    }
//                }
//            });

            // 5, 对关闭通道进行监听（只监听）
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
