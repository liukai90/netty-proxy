package com.spring.netty.springnetty.service;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public interface UserService {

    void proxy(final ChannelHandlerContext ctx, final FullHttpRequest msg);
}
