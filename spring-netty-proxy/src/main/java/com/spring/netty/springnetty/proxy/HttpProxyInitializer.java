package com.spring.netty.springnetty.proxy;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import org.springframework.stereotype.Component;

public class HttpProxyInitializer extends ChannelInitializer {
    private Channel clientChannel;

    public HttpProxyInitializer(Channel clientChannel) {
        this.clientChannel = clientChannel;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(new HttpClientCodec());
        ch.pipeline().addLast(new HttpObjectAggregator(1048576*10));
        ch.pipeline().addLast(new HttpProxyClientHandle(clientChannel));
    }
}
