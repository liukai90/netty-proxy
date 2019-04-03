package com.liukai.netty.handle;

import com.liukai.netty.service.OtherService;
import com.liukai.netty.service.RouterServerService;
import com.liukai.netty.service.UserClientService;
import com.liukai.netty.service.impl.OtherServiceImpl;
import com.liukai.netty.service.impl.RouterServerServiceImpl;
import com.liukai.netty.service.impl.UserClientServiceImpl;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler.Sharable
public class HttpHandle extends ChannelInboundHandlerAdapter {

    private final Logger logger = LoggerFactory.getLogger(HttpHandle.class);
    private RouterServerService routerServerService = RouterServerServiceImpl.getInstance();
    private UserClientService userClientService = UserClientServiceImpl.getInstance();
    private OtherService otherService = OtherServiceImpl.getInstance();



    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        super.channelRead(ctx, msg);
            logger.info(msg.toString());
            FullHttpRequest request = null;
            if (msg instanceof FullHttpRequest){
                request = (FullHttpRequest) msg;
            }
            int client = request.headers().getInt("client",0);
            logger.info("client:"+client);
            if (client == 1){
                logger.info("client call");
                userClientService.proxy(ctx,request);
            }else if (client == 2){
                logger.info("server call");
                routerServerService.keepAlive(ctx,request);
            }else {
                logger.info("other call");
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
