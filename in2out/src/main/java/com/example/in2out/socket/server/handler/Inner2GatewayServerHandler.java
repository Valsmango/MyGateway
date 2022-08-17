package com.example.in2out.socket.server.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.in2out.entity.OuterXmlInfoCache;
import com.example.in2out.entity.TestRequestEntity;
import com.example.in2out.service.OuterXmlFileService;
import com.example.in2out.service.impl.OuterXmlFileServiceImpl;
import com.example.in2out.socket.client.Gateway2OuterClient;
import com.example.in2out.utils.XmlTrans;
import com.example.in2out.utils.XmlUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Inner2GatewayServerHandler extends ChannelInboundHandlerAdapter {
//    @Autowired
    XmlUtils xmlUtils = new XmlUtils();
    @Autowired
    XmlTrans xmlTrans;

//    @Autowired
    OuterXmlFileService outerXmlFileService = new OuterXmlFileServiceImpl();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Gateway收到来自内部的连接（HTTP）：" + ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest fullHttpRequest = (FullHttpRequest) msg;
            // 1.在这里调用Xml相关的工具、Service层的业务字段分析，来解析http的具体内容（json转xml）
            ByteBuf buffer = fullHttpRequest.content();
            String str = buffer.toString(CharsetUtil.UTF_8);
            JSONObject jsonObjIn = JSON.parseObject(str);

            // 加业务逻辑


            // 假设这里需要根据不同的服务来解析对象
            TestRequestEntity personInfo = JSONObject.toJavaObject(jsonObjIn, TestRequestEntity.class);
            System.out.println("收到消息（HTTP + JSON）:" + str +" ，并得到解析后的JAVA对象：" +personInfo.toString());

            // 2. 解析要调用外部的接口，在这里触发gateway2outer的通信 --> 去map里面找gateway2outer的channel/pipeline，没有才创建连接
            //      基于Gateway2OuterClient创建
            // if (map.containsKey( )) : 直接返回一个TCP的channel
            // else ：触发创建TCP连接
            Gateway2OuterClient gateway2OuterClient = new Gateway2OuterClient();
            gateway2OuterClient.get2OuterClient();

            // 3. 如果2顺利，读取Gateway2OuterClient中接收的内容
            // 思考：这里会出现阻塞问题吗？可以异步处理吗，NIO???？
            OuterXmlInfoCache outerXml = outerXmlFileService.getOuterXml("127.0.0.1", 8866);
            // 4.将读取到的内容再按照http协议进行封装（xml转json），write&flush给Inner

            String info = outerXml.getInfo();
            Map<String, Object> map = xmlUtils.multilayerXmlToMap(info);
            JSONObject jsonObjOut = new JSONObject(map);

            // 构造一个Http响应
            ByteBuf content = Unpooled.copiedBuffer(jsonObjOut.toJSONString(), CharsetUtil.UTF_16);
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

            // 返回
            ChannelFuture channelFuture = ctx.writeAndFlush(response);
            System.out.println("Gateway已经将外部模块发来的信息转发给内部模块（HTTP + JSON）：" + jsonObjOut.toJSONString());
            ctx.close();
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        System.out.println("进入 Inner2GatewayServer 的 channelReadComplete 方法");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
