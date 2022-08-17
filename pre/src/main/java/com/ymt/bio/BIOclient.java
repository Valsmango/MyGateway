package com.ymt.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Scanner;

public class BIOclient {
    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() ->{
            try {

                handler1("ahhhh");
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread1.start();
//        Thread.sleep(300);
        Thread thread2 = new Thread(() ->{
            try {
                handler2("hjsakfkajk");
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread2.start();

    }
    private static void handler1(String sayHello) throws IOException, InterruptedException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("localhost", 6666));
        int i = 0;
        while (i < 3) {
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(sayHello.getBytes());
            Thread.sleep(1000);
            i++;
            //            InputStream inputStream = socket.getInputStream();
            //            byte[] bytes = new byte[1024];
            //            while (true) {
            //                int read = 0;
            //                try {
            //                    read = inputStream.read(bytes);
            //                    if (read!=-1) {
            //                        System.out.println(new String(bytes,0,read));
            //                    } else {
            //                        break;
            //                    }
            //                } catch (IOException e) {
            //                    e.printStackTrace();
            //                } finally {
            //                    socket.close();
            //                }
            //            }
        }
        socket.close();
    }
    private static void handler2(String sayHello) throws IOException, InterruptedException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("localhost", 6666));
        int i = 0;
        while (i < 3) {
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(sayHello.getBytes());
            i++;
            //            InputStream inputStream = socket.getInputStream();
            //            byte[] bytes = new byte[1024];
            //            while (true) {
            //                int read = 0;
            //                try {
            //                    read = inputStream.read(bytes);
            //                    if (read!=-1) {
            //                        System.out.println(new String(bytes,0,read));
            //                    } else {
            //                        break;
            //                    }
            //                } catch (IOException e) {
            //                    e.printStackTrace();
            //                } finally {
            //                    socket.close();
            //                }
            //            }
        }
        socket.close();
    }






}
