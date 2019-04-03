package com.spring.netty.springnetty.service.impl;

import com.spring.netty.springnetty.serializer.impl.JSONSerializer;
import com.spring.netty.springnetty.service.RouterService;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.sun.deploy.net.HttpRequest.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

@Component
public class RouterServiceImpl implements RouterService {
    private final Logger logger = LoggerFactory.getLogger(RouterServiceImpl.class);

    @Autowired
    private JSONSerializer jsonSerializer;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void keepAlive(ChannelHandlerContext ctx, FullHttpRequest msg) {
        String key = msg.headers().get("key");
        String address = msg.headers().get("Host");
        String s[] = address.split(":");

        logger.info("key=" + key + ";" + "address=" + address + ";" + "host:" + s[0] + ";" + "port:" + s[1] + ";");
        Map<String, String> addressMap = new HashMap<>();
        addressMap.put("host", s[0]);
        addressMap.put("port", s[1]);

        stringRedisTemplate.boundHashOps(key).putAll(addressMap);
        stringRedisTemplate.boundHashOps(key).expire(30L, TimeUnit.SECONDS);

        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(jsonSerializer.serialize(addressMap)));
        response.headers().set(CONTENT_TYPE, "application/json;charset=UTF-8");
        response.headers().set(ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        response.headers().set(ACCESS_CONTROL_ALLOW_HEADERS,"*");
        response.headers().set(ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        response.headers().setInt(CONTENT_LENGTH, response.content().readableBytes());

        boolean keepAlive = HttpUtil.isKeepAlive(msg);
        if (!keepAlive) {
            ctx.write(response).addListener(ChannelFutureListener.CLOSE);
        } else {
            response.headers().set(CONNECTION, KEEP_ALIVE);
            ctx.write(response);
        }
    }
}
