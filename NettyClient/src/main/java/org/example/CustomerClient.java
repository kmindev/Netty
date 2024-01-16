package org.example;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.net.InetSocketAddress;

public class CustomerClient {

    private static final int SERVER_PORT = 11111;
    private final String host;
    private final int port;

    private Channel channel;
    private EventLoopGroup eventLoopGroup;

    public CustomerClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() throws InterruptedException {
        // NIO를 사용하기 위해 EventLoopGroup 생성
        eventLoopGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("client"));

        // Bootstrap 생성 및 설정
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress(host, port))
                .handler(new ClientInitializer());

        channel = bootstrap.connect().sync().channel();
        channel.closeFuture().sync();

    }

    public void close() {
        eventLoopGroup.shutdownGracefully();
    }

    public static void main(String[] args) {
        CustomerClient customerClient = new CustomerClient("127.0.0.1", SERVER_PORT);

        try {
            customerClient.connect();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            customerClient.close();
        }
    }

}