package com.ymt.netty05;

import com.ymt.netty04.GroupChatClient;
import com.ymt.netty04.GroupChatClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

public class ImprChatClient {
    private final String host;

    private final int port;

    public ImprChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast("decoder", new StringDecoder());
                            pipeline.addLast("encoder", new StringEncoder());
                            pipeline.addLast(new ImprChatClientHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        System.out.println("连接聊天室服务器成功");
                    } else {
                        System.out.println("连接聊天室服务器失败");
                    }
                }
            });
            Scanner sc = new Scanner(System.in);
            Channel channel = channelFuture.channel();
//            while (sc.hasNextLine()) {
                int id = Integer.valueOf(sc.nextLine());
                String msg = sc.nextLine();
                Message message = new Message(id, msg);
                // 通过channel发送
                channel.writeAndFlush(message); // 这里要把所有信息都统一，不能一些是String，一些是Message，并且封装好对应的Encoder、Decoder
//            }
        } finally{
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new ImprChatClient("127.0.0.1", 8668).run();
    }
}
