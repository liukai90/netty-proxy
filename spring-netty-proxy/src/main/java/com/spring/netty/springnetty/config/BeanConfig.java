package com.spring.netty.springnetty.config;

import com.spring.netty.springnetty.handler.HttpHandle;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:/netty.properties")
public class BeanConfig {

    @Value("${boss.thread.count}")
    private int bossCount;

    @Value("${worker.thread.count}")
    private int workerCount;

    @Value("${server.port}")
    private int serverPort;

    @Value("${so.keepalive}")
    private boolean keepAlive;

    @Value("${tcp.nodelay}")
    private boolean tcpNodelay;

    @Value("${so.backlog}")
    private int backlog;

    @Value("${maxContentLength}")
    private int maxContentLength;


    @Autowired
    private HttpHandle httpHandle;

    @Bean(name = "port")
    public Integer port(){
        return serverPort;
    }

    @Bean(name = "bossGroup",destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup bossGroup(){
        return new NioEventLoopGroup(bossCount);
    }

    @Bean(name = "workerGroup",destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup workerGroup(){
        return new NioEventLoopGroup(workerCount);
    }

    @Bean(name = "serverCodec")
    public HttpServerCodec serverCodec(){
        return new HttpServerCodec();
    }

    @Bean(name = "aggregator")
    public HttpObjectAggregator aggregator(){
        return new HttpObjectAggregator(maxContentLength);
    }

    @Bean(name = "channelInitializer")
    public ChannelInitializer channelInitializer(){
        return new ChannelInitializer() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                ChannelPipeline channelPipeline = channel.pipeline();
                channelPipeline.addLast(serverCodec());
                channelPipeline.addLast(aggregator());
                channelPipeline.addLast(httpHandle);
            }
        };
    }

    @Bean(name = "serverBootstrap")
    public ServerBootstrap serverBootstrap(){

        return new ServerBootstrap().group(bossGroup(),workerGroup()).
                channel(NioServerSocketChannel.class).
                handler(new LoggingHandler(LogLevel.INFO)).
                childHandler(channelInitializer()).
                option(ChannelOption.SO_BACKLOG,backlog).
                childOption(ChannelOption.TCP_NODELAY,tcpNodelay).
                childOption(ChannelOption.SO_KEEPALIVE,keepAlive);

    }

}
