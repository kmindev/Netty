package org.example.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Component
public class TelnetServer {

    @Autowired
    private InetSocketAddress tcpPort;

    @Value("${boss.thread.count}")
    private int bossCount;

    @Value("${worker.thread.count}")
    private int workerCount;

    public void start() throws InterruptedException {
        ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
        EventLoopGroup bossEventLoopGroup = new NioEventLoopGroup(bossCount);
        EventLoopGroup workerEventLoopGroup = new NioEventLoopGroup(workerCount);

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap
                    .group(bossEventLoopGroup, workerEventLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new TelnetServerInitializer());

            ChannelFuture channelFuture = bootstrap.bind(tcpPort).sync();
            channels.add(channelFuture.channel()); // 채널 그룹에 추가
            channelFuture.channel().closeFuture().sync(); // 종료될 때 까지 대기

        } finally {
            channels.close().awaitUninterruptibly();
            workerEventLoopGroup.shutdownGracefully().awaitUninterruptibly();
            bossEventLoopGroup.shutdownGracefully().awaitUninterruptibly();
        }
    }
}