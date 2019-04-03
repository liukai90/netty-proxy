package com.spring.netty.springnetty;

import com.spring.netty.springnetty.bootstrap.HttpServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringNettyApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(SpringNettyApplication.class, args);
		HttpServer httpServer = configurableApplicationContext.getBean(HttpServer.class);
		httpServer.serverStart();
	}

}
