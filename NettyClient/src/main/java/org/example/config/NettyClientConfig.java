package org.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.InetSocketAddress;

@EnableScheduling
@ComponentScan("org.example")
@PropertySource("config/application.properties")
@Configuration
public class NettyClientConfig {

    @Value("${tcp.host}")
    private String host;

    @Value("${tcp.port}")
    private int tcpPort;

    @Bean
    public InetSocketAddress serverAddress() {
        return new InetSocketAddress(host, tcpPort);
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
