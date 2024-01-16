package org.example;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;

public class RestaurantServer {

    private static final int SERVER_PORT = 11111;

    private final ChannelGroup channelGroup = new DefaultChannelGroup("RestaurantServer", GlobalEventExecutor.INSTANCE);
    private EventLoopGroup bossEventLoopGroup;
    private EventLoopGroup workerEventLoopGroup;

    public void startServer() throws InterruptedException {
        bossEventLoopGroup = new NioEventLoopGroup(1);
        workerEventLoopGroup = new NioEventLoopGroup(1);

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap
                    .group(bossEventLoopGroup, workerEventLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new RestaurantServerInitializer());

            ChannelFuture channelFuture = bootstrap.bind(SERVER_PORT).sync();

            Channel channel = channelFuture.channel(); // 채널이 활성화 되면
            channelGroup.add(channel); // 이벤트 루프 그룹에 추가

            ChannelFuture closeFuture = channelFuture.channel().closeFuture().sync();
            closeFuture.channel().close();

        } finally {
            close();
        }

    }

    private void close() {
        channelGroup.close().awaitUninterruptibly();
        workerEventLoopGroup.shutdownGracefully().awaitUninterruptibly();
        bossEventLoopGroup.shutdownGracefully().awaitUninterruptibly();
    }


    public static void main(String[] args) throws InterruptedException {
        new RestaurantServer().startServer();
    }
}