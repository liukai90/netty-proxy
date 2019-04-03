package com.liukai.netty.service.impl;

import com.liukai.netty.data.JedisClient;
import com.liukai.netty.proxyclient.HttpProxyInitializer;
import com.liukai.netty.service.UserClientService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class UserClientServiceImpl implements UserClientService {

    private final Logger logger = LoggerFactory.getLogger(UserClientServiceImpl.class);

    private static UserClientServiceImpl userClientService = null;
    private JedisClient jedisClient = new JedisClient();

    private UserClientServiceImpl(){

    }
    public void proxy(final ChannelHandlerContext ctx,final FullHttpRequest msg) {

        String key = msg.headers().get("key");
        Map<String,String> address = jedisClient.hGetAll(key);
        String host = address.get("host");
        int port = Integer.parseInt(address.get("port"));
        logger.info(host+":"+port);

        //连接至目标服务器
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(ctx.channel().eventLoop()) // 注册线程池
                .channel(ctx.channel().getClass()) // 使用NioSocketChannel来作为连接用的channel类
                .handler(new HttpProxyInitializer(ctx.channel()));

        ChannelFuture cf = bootstrap.connect(host, port);
        cf.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {

                    future.channel().writeAndFlush(msg);
                } else {
                    ctx.channel().close();
                }
            }
        });
    }

    public static UserClientServiceImpl getInstance(){
        if (userClientService == null){
            userClientService = new UserClientServiceImpl();
        }
        return userClientService;
    }


}
