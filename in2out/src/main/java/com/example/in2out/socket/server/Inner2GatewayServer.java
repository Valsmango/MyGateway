package com.example.in2out.socket.server;

import com.example.in2out.socket.server.handler.Inner2GatewayServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 是一个http的Server;
 *
 * 这里需要修改为 ctx.channel().eventLoop().execute()然后跑任务的形式；
 *
 * 测试方法：
 * cmd：curl http://localhost:8888 -H "Content-Type:application/json" -X GET -d "{\“NAM\”:\"1111\",\"LT_CRDT_ID\":\"2222\",\"TP\":\"3333\"}" --output E:/test.txt
 */
@Component
public class Inner2GatewayServer implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(8);
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // 加入HTTP的编码/解码器
                            pipeline.addLast(new HttpServerCodec());
                            // 消息聚合器，添加消息聚合器之后，没办法接收innerclient的http数据；但如果不加聚合器，则只能收到头字段，不能收到来自cur的全部内容
                            pipeline.addLast(new HttpObjectAggregator(65535));
//                            pipeline.addLast(new ChunkedWriteHandler());
                            // 增加自定义的处理器
                            pipeline.addLast(new Inner2GatewayServerHandler());
                        }
                    });
            System.out.println("—————————————————— Inner2Gateway HttpServer Bootstrap is READY！----------------");
            ChannelFuture channelFuture = bootstrap.bind(8888).sync();

            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}