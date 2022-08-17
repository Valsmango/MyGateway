package com.ymt.netty05;

import io.netty.channel.Channel;

public class Tmp {
}
class Message {
    int partnerId;
    String msg;
    Message(int partnerId, String msg) {
        this.partnerId = partnerId;
        this.msg = msg;
    }
}
class User {
    int id;
    Channel channel;
    User(int id, Channel channel) {
        this.id = id;
        this.channel = channel;
    }
}
