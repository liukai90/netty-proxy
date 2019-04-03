package com.liukai.netty.service;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public interface OtherService {

    void other(ChannelHandlerContext ctx, FullHttpRequest msg);
}
