package com.example.in2out.socket.client;

import com.example.in2out.socket.client.handler.Gateway2OuterClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 这里要修改：
 * 1，传入参数：解析出来的目标外部接口的host和port （or唯一标记）
 * 2.不能实现XommandLineRunner接口，而是要在调用这一类的一个暴露出来的接口，才开始connect
 * 3.Handler中收到并读取的数据也得存起来，然后交给这里，再返回给上一调用接口
 */
@Component
@NoArgsConstructor
@AllArgsConstructor
public class Gateway2OuterClient {

    private String host;
    private int port;

    public SocketChannel get2OuterClient() throws InterruptedException {
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
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new Gateway2OuterClientHandler()); // 将handler扔进pipeline中，pipeline相当于比channel更厉害的一个东西
                        }
                    });
            System.out.println("—————————————————— Gateway2Outer Client‘s Bootstrap is READY！----------------");

            // 激活启动器，将服务器端的消息包装成一个ChannelFuture对象（虽然调用的是sync方法，但是ChannelFuture是异步的）
            // 这里的host和port都来自上一个http报文的读取
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8866).sync();
            // 进行关闭监听
            channelFuture.channel().closeFuture().sync();
        } finally {    // 无catch是因为抛出了异常
            eventExecutors.shutdownGracefully();
        }
        return null;
    }

}


/*

    private String host;
    private int port;

    public SocketChannel get2OuterClient(String host, int port) {
        this.host = host;
        this.port = port;
        // 将run中的内容抽取到这里

        return null;
    }

    @Override
    public void run(String... args) throws Exception {
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
                            socketChannel.pipeline().addLast(new Gateway2OuterClientHandler()); // 将handler扔进pipeline中，pipeline相当于比channel更厉害的一个东西
                        }
                    });
            System.out.println("—————————————————— Gateway2Outer Client‘s Bootstrap is READY！----------------");

            // 激活启动器，将服务器端的消息包装成一个ChannelFuture对象（虽然调用的是sync方法，但是ChannelFuture是异步的）
            // 这里的host和port都来自上一个http报文的读取
            ChannelFuture channelFuture = bootstrap.connect("localhost", 8866).sync();
            // 进行关闭监听
            channelFuture.channel().closeFuture().sync();
        } finally {    // 无catch是因为抛出了异常
            eventExecutors.shutdownGracefully();
        }
    }

*/