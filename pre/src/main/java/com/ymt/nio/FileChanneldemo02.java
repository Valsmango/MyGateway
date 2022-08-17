package com.ymt.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChanneldemo02 {
    public static void main(String[] args) throws Exception {
        File file = new File("C:\\Users\\DELL\\Desktop\\test01.txt");
        FileInputStream input = new FileInputStream(file);
        FileChannel channel = input.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate((int)file.length());
        channel.read(byteBuffer);
//        byteBuffer.flip();
//        byte[] bytes = new byte[(int)file.length()];
//        int i = 0;
//        while(byteBuffer.hasRemaining()) {
//            bytes[i++] = byteBuffer.get();
//        }
//        System.out.println(new String(bytes));
        System.out.println(new String(byteBuffer.array()));
        input.close();
    }
}
