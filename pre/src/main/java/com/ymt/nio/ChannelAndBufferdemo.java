package com.ymt.nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class ChannelAndBufferdemo {
    public static void main(String[] args) throws IOException {
        // 可以让文件直接在内存（堆外内存）中修改，所以OS不需要拷贝一次
        RandomAccessFile randomAccessFile = new RandomAccessFile("C:\\Users\\DELL\\Desktop\\test01.txt","rw");
        FileChannel channel = randomAccessFile.getChannel();

        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        mappedByteBuffer.put(0, (byte) 'Y');
        mappedByteBuffer.put(4, (byte) 'a');

        randomAccessFile.close();
    }
}
