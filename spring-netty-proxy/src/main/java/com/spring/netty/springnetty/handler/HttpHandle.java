package com.spring.netty.springnetty.handler;

import com.spring.netty.springnetty.service.OtherService;
import com.spring.netty.springnetty.service.RouterService;
import com.spring.netty.springnetty.service.UserService;
import com.spring.netty.springnetty.value.Values;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class HttpHandle extends ChannelInboundHandlerAdapter {

    private final Logger logger = LoggerFactory.getLogger(HttpHandle.class);

    @Autowired
    private RouterService routerService;

    @Autowired
    private UserService userService;

    @Autowired
    private OtherService otherService;


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        logger.info(msg.toString());
        FullHttpRequest request = null;
        if (msg instanceof FullHttpRequest){
            request = (FullHttpRequest) msg;
        }
        int client = Integer.parseInt(request.headers().get("client","0"));
        logger.info("client:"+client);

        if (client == Values.REQUEST_TYPE_APP){
            userService.proxy(ctx,request);
        }else if (client == Values.REQUEST_TYPE_ROUTER){
            routerService.keepAlive(ctx,request);
        }else {
            logger.error("非法请求");
            otherService.other(ctx,request);
        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
