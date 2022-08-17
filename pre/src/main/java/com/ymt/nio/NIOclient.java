package com.ymt.nio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOclient {
    public static void main(String[] args) throws IOException, InterruptedException {
        // 这里不能直接基于socket.getOutputStream();进行传输，connection会被拒绝
        Thread thread1 = new Thread(() ->{
            try {
                handler2("ahhhh");
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread1.start();

        Thread thread2 = new Thread(() ->{
            try {
                Thread.sleep(5000);
                handler1("hjsakfkajk");
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread2.start();
    }
    private static void handler1(String sayHello) throws IOException, InterruptedException {
//        Socket socket = new Socket();
//        socket.connect(new InetSocketAddress("localhost", 7000));
//        SocketChannel channel = socket.getChannel();
//        channel.configureBlocking(false);   // 不能这样写，这样会报错，所以只能用handler2，所以要设置非阻塞的，必须用open方法来开channel
        SocketChannel channel = SocketChannel.open();
        InetSocketAddress socketAddress = new InetSocketAddress("localhost", 7000);
        channel.configureBlocking(false);
        if (!channel.connect(socketAddress)) {   // 连接失败
            while (!channel.finishConnect()) {
                System.out.println("handler1 -- 因为连接需要事件，所以客户端不会阻塞……");
            }
        }
        Thread.sleep(3000);
        ByteBuffer byteBuffer = ByteBuffer.wrap(sayHello.getBytes());
        channel.write(byteBuffer);
        System.in.read();   // 为什么加了这一句就会停呢？
        channel.close();
    }
    private static void handler2(String sayHello) throws IOException, InterruptedException {
        SocketChannel channel = SocketChannel.open();
        InetSocketAddress socketAddress = new InetSocketAddress("localhost", 7000);
        channel.configureBlocking(false);
        if (!channel.connect(socketAddress)) {
            while (!channel.finishConnect()) {
                System.out.println("handler2 -- 因为连接需要事件，所以客户端不会阻塞……");
            }
        }
        ByteBuffer byteBuffer = ByteBuffer.wrap(sayHello.getBytes());
        channel.write(byteBuffer);
        System.in.read();   // 为什么加了这一句就会停呢？
        channel.close();
    }
}
