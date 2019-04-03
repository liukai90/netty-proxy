package com.spring.netty.springnetty.service;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public interface RouterService {
    void keepAlive(final ChannelHandlerContext ctx, final FullHttpRequest msg);
}
