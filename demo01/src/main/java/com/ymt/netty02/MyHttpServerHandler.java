package com.ymt.netty02;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

public class MyHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {
        // 判断是否是HttpRequest请求
        if (httpObject instanceof HttpRequest) {
            System.out.println("msg类型 " + httpObject.getClass());
            // 获取到用户发送的报文
            HttpRequest httpRequest = ((HttpRequest) httpObject);
            URI uri = new URI(httpRequest.uri());
            if ("/favicon.ico".equals(uri.getPath())) {
                System.out.println("请求了不会响应的资源：favicon.ico");
                return;
            }

            // 构造一个Http响应
            ByteBuf content = Unpooled.copiedBuffer("Hello, 我是服务器~", CharsetUtil.UTF_16);
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
            // 返回
            channelHandlerContext.writeAndFlush(response);
        }
    }

}
