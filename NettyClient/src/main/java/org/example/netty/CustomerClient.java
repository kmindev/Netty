package org.example.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Component
public class CustomerClient {

    @Autowired
    private InetSocketAddress serverAddress;

    @Value("${worker.thread.count}")
    private int workerCount;

    @Scheduled(fixedDelayString = "${scheduled.delay}", initialDelayString = "${scheduled.first}")
    @Async
    public void connect() throws InterruptedException {
        EventLoopGroup eventLoopGroup;
        eventLoopGroup = new NioEventLoopGroup(workerCount);
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .remoteAddress(serverAddress)
                .handler(new ClientInitializer());

        Channel channel = bootstrap.connect().sync().channel();
        channel.closeFuture().sync();

        eventLoopGroup.shutdownGracefully();
    }

}