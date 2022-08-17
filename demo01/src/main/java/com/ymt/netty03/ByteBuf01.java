package com.ymt.netty03;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

public class ByteBuf01 {
    public static void main(String[] args) {
        ByteBuf buffer = Unpooled.buffer(10);  // 创建一个ByteBuf： 内部含一个byte[10]
        ByteBuf buffer2 = Unpooled.copiedBuffer("Hello,world!", CharsetUtil.UTF_8);

        for (int i = 0; i < 10; i++) {
            buffer.writeByte(buffer2.readByte());
        }
        for (int i = 0; i < 10; i++) {
            System.out.print(((char) buffer.readByte()));
        }
        System.out.println("");
        System.out.println(buffer2.getCharSequence(10,2, CharsetUtil.UTF_8));   // 从第10个byte开始，读2个
    }
}
