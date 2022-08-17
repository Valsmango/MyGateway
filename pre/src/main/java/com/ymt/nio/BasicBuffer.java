package com.ymt.nio;

import java.nio.IntBuffer;

public class BasicBuffer {
    public static void main(String[] args) {
        // 分析NIO中的buffer，buffer是一个根接口
        // 该接口的实现类包括intbuffer、floatbuffer、bytebuffer、charbuffer、doublebuffer、shortbuffer、longbuffer
        IntBuffer intBuffer = IntBuffer.allocate(5);
        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(i*2);
        }
        intBuffer.flip();   // 读写转换，导致buffer中的各种属性进行转换
        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());
        }
        intBuffer.clear();  // clear并不会真正删除数据
        System.out.println(intBuffer.get(1));
    }
}
