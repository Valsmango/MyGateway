package com.ymt.netty06;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

public class HeartBeatPostServerHandler extends ChannelInboundHandlerAdapter {
    // 没有收到数据就直接继承ChannelInboundHandlerAdapter
    // 收到了数据就Simplexxxx
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            // 向下转型
            IdleStateEvent event = ((IdleStateEvent) evt);
            String eventType = null;
            switch (event.state()) {
                case READER_IDLE:
                    eventType = "读空闲";
                    break;
                case WRITER_IDLE:
                    eventType = "写空闲";
                    break;
                case ALL_IDLE:
                    eventType = "读写空闲";
                    break;
            }
            System.out.println(ctx.channel().remoteAddress() + "----超时----" + eventType);
            System.out.println("服务器处理中……");
            ctx.channel().close();
        }
    }

}
