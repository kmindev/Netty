package org.example;


import org.example.config.NettyClientConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class NettyClientStarter {
    public static void main(String[] args) {
        ApplicationContext ac = new AnnotationConfigApplicationContext(NettyClientConfig.class);

    }
}
