package com.spring.netty.springnetty.bootstrap;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Data
public class HttpServer {

    @Autowired
    @Qualifier(value = "serverBootstrap")
    private ServerBootstrap serverBootstrap;

    @Autowired
    @Qualifier(value = "port")
    private int port;

    public void serverStart(){
        try {
            Channel channel = serverBootstrap.bind(port).sync().channel();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
