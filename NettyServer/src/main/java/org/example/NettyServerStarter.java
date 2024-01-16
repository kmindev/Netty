package org.example;

import org.example.config.NettyServerConfig;
import org.example.netty.RestaurantServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class NettyServerStarter {
    public static void main(String[] args) {
        try {
            ApplicationContext ac = new AnnotationConfigApplicationContext(NettyServerConfig.class);

            RestaurantServer server = ac.getBean(RestaurantServer.class);
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
