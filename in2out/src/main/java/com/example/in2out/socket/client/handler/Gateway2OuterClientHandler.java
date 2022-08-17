package com.example.in2out.socket.client.handler;

import com.example.in2out.entity.OuterXmlInfoCache;
import com.example.in2out.service.OuterXmlFileService;
import com.example.in2out.service.impl.OuterXmlFileServiceImpl;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;

/**
 * 向外传输xml文件来请求对应的服务
 */
@Component
public class Gateway2OuterClientHandler extends ChannelInboundHandlerAdapter {
//    @Autowired
    OuterXmlFileService outerXmlFileService = new OuterXmlFileServiceImpl();

    String host = "127.0.0.1";

    int port = 8866;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("-----已连接上外部服务模块，准备发送xml文件进行服务请求----");
        // 1.调用服务模块，组装xml报文
        SAXReader reader = new SAXReader();
        Document dc= reader.read(new File("E:\\ymt_Projects\\IDEA_Proj\\Netty\\in2out\\src\\data\\test.xml"));
        String xmlFile = dc.asXML();
        ByteBuf buffer = Unpooled.copiedBuffer(xmlFile.getBytes(StandardCharsets.UTF_8));
//        Element e = dc.getRootElement();
//        // 获取迭代器
//        Iterator it = e.elementIterator();
//        // 遍历迭代器，获取根节点信息
//        while(it.hasNext()){
//            Element elem = (Element) it.next();
//            List<Attribute> atts= elem.attributes();
//            // 获取属性名和属性值
//            for (Attribute att : atts) {
//                System.out.println("节点名："+att.getName()+"节点值："+att.getValue());
//            }
//            Iterator itt = elem.elementIterator();
//            while(itt.hasNext()){
//                Element b = (Element) itt.next();
//                System.out.println("属性名："+b.getName()+"属性值："+b.getText());
//            }
//        }

        // 2. 发送xml报文，这里假设就是发送test.xml
        ChannelFuture channelFuture = ctx.writeAndFlush(buffer);
        System.out.println("向外部发送的信息(TCP + xml，前20个byte)：" + xmlFile.substring(0,20));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("-----已接收到外部返回的信息----");
        String message = ((ByteBuf)msg).toString(CharsetUtil.UTF_8);
        // 假设从外部接收到的数据要保存至数据库
        OuterXmlInfoCache outerXmlInfoCache = new OuterXmlInfoCache();
        outerXmlInfoCache.setHost(host);
        outerXmlInfoCache.setPort(port);
        outerXmlInfoCache.setInfo(message);
        outerXmlFileService.insertOuterXml(outerXmlInfoCache);
        System.out.println("已接收到外部模块（Outer Server: " + host + ":" + port + ")返回的信息(TCP + xml)：" + message);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 【 重要问题点 】
        // 直接关闭和外部的连接，但是问题是，如果数据不能一次发送完呢？需要改进为：告诉inner2gateway的server去数据库读消息
        System.out.println("-----外部模块信息已接收完毕----");
        ctx.channel().close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
