package com.example.demo.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;


/**
 * 功能描述：向Client发送Http报文
 */
@Component
public class InnerClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 测试报文内容（json数据）
        String test = "{\"NAM\":\"1111\",\"LT_CRDT_ID\":\"2222\",\"TP\":\"3333\"}";

        ByteBuf buffer = Unpooled.copiedBuffer(test.getBytes(StandardCharsets.UTF_8));
        FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/127.0.0.1:8888", buffer);
        request.headers().set("Content-Type", "application/json;charset=UTF-8");
        request.headers().set("Content-Length", buffer.readableBytes());
        ChannelFuture channelFuture = ctx.channel().writeAndFlush(request);
        System.out.println("Inner Client 正在在向网关发送（HTTP Request + json）：" +test +"  --  "+request.getClass());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpResponse) {
            FullHttpResponse fullHttpResponse = ((FullHttpResponse) msg);
            ByteBuf buffer = fullHttpResponse.content();
            String str = buffer.toString(CharsetUtil.UTF_16);
            System.out.println("Inner Client 收到网关的回复信息（HTTP Response + json）：" + str +"  --  "+ msg.getClass());
        }
        ctx.close();
    }
}
