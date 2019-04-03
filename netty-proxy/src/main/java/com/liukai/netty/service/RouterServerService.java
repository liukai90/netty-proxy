package com.liukai.netty.service;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public interface RouterServerService {
    void keepAlive(final ChannelHandlerContext ctx, final FullHttpRequest msg);
}
