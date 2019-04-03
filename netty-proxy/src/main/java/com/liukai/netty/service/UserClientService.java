package com.liukai.netty.service;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public interface UserClientService {

    void proxy(final ChannelHandlerContext ctx, final FullHttpRequest msg);
}
