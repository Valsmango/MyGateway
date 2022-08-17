package com.ymt.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class BIOserver {
    public static void main(String[] args) throws IOException {
        // java IO编程和网络编程 ———— BIO模拟
        // 创建一个线程池，如果有客户端连接，就分配一个线程与之通信
        // 然后监听客户端的数据读写，如果没有读写，就一直等一直等 --> 阻塞
        ThreadPoolExecutor poll = new ThreadPoolExecutor(
                10, 15,
                3, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(10),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
        // 创建Socket进行网络连接
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务器启动了");
        while (true) {  // 阻塞
            System.out.println("服务器正在监听，等待客户端连接");
            final Socket accept = serverSocket.accept();
            poll.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("连接到一个客户端");
                    // 和客户端进行通信
                    handler(accept);
                }
            });
        }


    }
    public static void handler(Socket socket) {
        // 通过socket获取输入流
        try {
            byte[] bytes = new byte[1024];
            InputStream inputStream = socket.getInputStream();
            while (true) { // 循环读取

                int read = inputStream.read(bytes);
                if (read != -1) {
                    System.out.println("current --- " + Thread.currentThread().getId() + ": " + new String(bytes, 0 ,read));
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("关闭和client的连接");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
