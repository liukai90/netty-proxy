package com.spring.netty.springnetty.service.impl;

import com.spring.netty.springnetty.proxy.HttpProxyInitializer;
import com.spring.netty.springnetty.serializer.impl.JSONSerializer;
import com.spring.netty.springnetty.service.UserService;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

@Component
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private JSONSerializer jsonSerializer;

    public void proxy(final ChannelHandlerContext ctx, final FullHttpRequest msg) {

        String key = msg.headers().get("key");
        Map<Object, Object> address =  stringRedisTemplate.boundHashOps(key).entries();
        String host = (String) address.get("host");
        int port = Integer.parseInt((String)address.get("port"));

        //连接至目标服务器
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(ctx.channel().eventLoop()) // 注册线程池
                .channel(ctx.channel().getClass()) // 使用NioSocketChannel来作为连接用的channel类
                .handler(new HttpProxyInitializer(ctx.channel()));

        ChannelFuture cf = bootstrap.connect(host, port);
        cf.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    //改变
                    future.channel().writeAndFlush(msg);
                } else {
                    logger.info("连接失败");
                    byte [] content = jsonSerializer.serialize("连接失败");
                    ctx.channel().writeAndFlush(new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND, Unpooled.wrappedBuffer(content)));
                    ctx.channel().close();
                }
            }
        });
    }

}
