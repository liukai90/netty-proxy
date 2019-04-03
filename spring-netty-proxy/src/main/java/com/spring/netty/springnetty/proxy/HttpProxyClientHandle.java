package com.spring.netty.springnetty.proxy;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpProxyClientHandle extends ChannelInboundHandlerAdapter {

    private final Logger logger = LoggerFactory.getLogger(HttpProxyClientHandle.class);

    private Channel clientChannel;

    public HttpProxyClientHandle(Channel clientChannel) {
        this.clientChannel = clientChannel;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("客户端消息："+msg.toString());
        FullHttpResponse response = (FullHttpResponse) msg;
        clientChannel.writeAndFlush(response);
    }
}
