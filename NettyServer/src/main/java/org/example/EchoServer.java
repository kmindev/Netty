package org.example;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.net.InetSocketAddress;

public class EchoServer {

    private static final int SERVER_PORT = 11111;

    private final ChannelGroup channelGroup = new DefaultChannelGroup("server", GlobalEventExecutor.INSTANCE);
    private EventLoopGroup bossEventLoopGroup;
    private EventLoopGroup workerEventLoopGroup;

    public void startServer() {

        // EventLoop은 새로운 이벤트를 반복적으로 확인하는 루프
        // EventLoop 그룹 => EventLoopGroup
        bossEventLoopGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("boss")); // listen 용 그룹
        workerEventLoopGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("worker")); // 데이터 처리용 그룹

        // Bootstrap: Netty가 작동할 때 기본적으로 설정 해야하는 클래스
        ServerBootstrap bootstrap = new ServerBootstrap(); // ServerBootStrap 생성 및 설정

        bootstrap.group(bossEventLoopGroup, workerEventLoopGroup); // EventGroup 할당

        bootstrap.channel(NioServerSocketChannel.class); // NIO 소켓을 이용한 채널을 생성

        // accept 되어 생성되는 TCP 채널 설정
        // TCP_NODELAY,SO_KEEPALIVE 설정은 서버 소켓으로 연결되어 생성되는 Connection 특성
        bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);

        // client request를 처리할 Handler 등록
        bootstrap.childHandler(new EchoServerInitializer());

        try {
            // channel 생성 후 listen
            // Bootstrap 설정들과 함께 Port 바인딩 후 Listen
            ChannelFuture bindFuture = bootstrap.bind(new InetSocketAddress(SERVER_PORT)).sync();
            Channel channel = bindFuture.channel();
            channelGroup.add(channel);

            // channel이 닫힐 때까지 listen
            bindFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            close();
        }
    }

    private void close() {
        channelGroup.close().awaitUninterruptibly();
        workerEventLoopGroup.shutdownGracefully().awaitUninterruptibly();
        bossEventLoopGroup.shutdownGracefully().awaitUninterruptibly();
    }


    public static void main(String[] args) {
        new EchoServer().startServer();
    }
}