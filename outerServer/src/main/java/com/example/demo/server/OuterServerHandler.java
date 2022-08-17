package com.example.demo.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import org.springframework.stereotype.Component;

@Component
public class OuterServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 要基于dom4j读取一个gateway发送过来的xml文件
        String message = ((ByteBuf)msg).toString(CharsetUtil.UTF_8);
        System.out.println("Outer Server收到Gateway发送的数据（TCP + XML）：" + message);

        // 外部系统调用对应的业务返回一个xml文件，这里略

        // 然后将响应的内容以xml文件形式返回给gateway，假设随便发一个
        String xmlFile = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><SERVICE>THIS IS A MESSAGE FROM OUTER SERVER</SERVICE>";
        System.out.println("Outer Server向Gateway返回数据（TCP + XML）：" + xmlFile);
        ctx.writeAndFlush(Unpooled.copiedBuffer(xmlFile, CharsetUtil.UTF_8));
    }
}
