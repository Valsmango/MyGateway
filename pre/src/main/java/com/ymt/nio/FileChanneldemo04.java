package com.ymt.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChanneldemo04 {
    public static void main(String[] args) throws Exception {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        File readfile = new File("C:\\Users\\DELL\\Desktop\\test01.txt");   // 也可以是jpg！！！！
        File writefile = new File("C:\\Users\\DELL\\Desktop\\test03.txt");
        FileInputStream inputStream = new FileInputStream(readfile);
        FileOutputStream outputStream = new FileOutputStream(writefile);
        FileChannel readChannel = inputStream.getChannel();
        FileChannel writeChannel = outputStream.getChannel();
        writeChannel.transferFrom(readChannel,0,readChannel.size());
        writeChannel.close();
        readChannel.close();
        inputStream.close();
        outputStream.close();
    }
}
