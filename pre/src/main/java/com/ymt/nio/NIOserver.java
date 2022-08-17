package com.ymt.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOserver {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(7000));
        serverSocketChannel.configureBlocking(false);   // 注册之前一定要设置为非阻塞的

        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            if(selector.select(1000) == 0) { // 当前没有事件发生
                System.out.println("无连接");
                continue;
            }
            // 如果有事件发生
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                if (key.isAcceptable()) {   // 如果是SelectionKey.OP_ACCEPT事件
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功" + "; 目前的 key 数量：" + selector.keys().size());
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));  // 给他绑定一个buffer
                    System.out.println("客户端连接成功 --- 注册" + "; 目前的 key 数量：" + selector.keys().size());
                }
                if (key.isReadable()) { // 如果是SelectionKey.OP_READ事件
                    SocketChannel socketChannel = ((SocketChannel) key.channel());
                    ByteBuffer buffer = (ByteBuffer) key.attachment();  // 获取之前绑定的buffer
                    socketChannel.read(buffer);
                    System.out.println("客户端连接成功 --- 注册 --- 读取" + "; 目前的 key 数量：" + selector.keys().size() + "读取内容" +new String(buffer.array()));
                }
                keyIterator.remove();
            }
        }
    }
}
