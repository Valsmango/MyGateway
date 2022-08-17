package com.ymt.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChanneldemo03 {
    public static void main(String[] args) throws Exception {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        File readfile = new File("C:\\Users\\DELL\\Desktop\\test01.txt");   // 也可以时jpg！！！！
        File writefile = new File("C:\\Users\\DELL\\Desktop\\test02.txt");
        FileInputStream inputStream = new FileInputStream(readfile);
        FileOutputStream outputStream = new FileOutputStream(writefile);
        FileChannel readChannel = inputStream.getChannel();
        FileChannel writeChannel = outputStream.getChannel();
        /* 原始：
        readChannel.read(byteBuffer);
        byteBuffer.flip();  // 这里必须要有这一句！！！
        writeChannel.write(byteBuffer);
        */
        // 改进后
        while (true) {
            byteBuffer.clear(); // 这一句很重要，不然eof会一直返回0（limit == position时返回0），并且不能继续读取，且循环不跳出
            int eof = readChannel.read(byteBuffer);
            if (eof == -1) {
                break;
            }
            byteBuffer.flip();  // 这里必须要有这一句！！！
            writeChannel.write(byteBuffer);
        }

        inputStream.close();
        outputStream.close();
    }
}
