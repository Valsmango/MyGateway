package com.ymt.nio;

import sun.nio.ch.FileChannelImpl;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChanneldemo01 {
    public static void main(String[] args) throws IOException {
        String str = "Hello,尚硅谷";
        ByteBuffer byteBuffer = ByteBuffer.allocate(15);    // 1个英文字符1字节，1个中文单字3字节
        byteBuffer.put(str.getBytes());
        // 将buffer中的数据写入Channel(输出流的一部分)
        FileOutputStream output = new FileOutputStream("C:\\Users\\DELL\\Desktop\\test01.txt");
        FileChannel channel = output.getChannel();
        byteBuffer.flip();
        channel.write(byteBuffer);  // 因为output把channel将给包裹了一层
        output.close();
    }
}
