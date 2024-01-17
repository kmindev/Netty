package org.example;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.Scanner;

public class TelnetClient {

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 11111;
    private static final int WORKER_COUNT = 1;

    private Channel channel = null;
    private EventLoopGroup eventLoopGroup;

    public void connect() throws InterruptedException {
        eventLoopGroup = new NioEventLoopGroup(WORKER_COUNT);
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress(HOST, PORT))
                .handler(new TelnetClientInitializer());

        channel = bootstrap.connect().sync().channel();

    }

    private void close() {
        eventLoopGroup.shutdownGracefully();
    }

    private void start() throws InterruptedException {
        Scanner sc = new Scanner(System.in);

        String message;
        ChannelFuture future = null;

        while (true) {
            // 사용자 입력
            message = sc.nextLine();

            if(!channel.isActive()) {
                System.out.println("연결이 종료되었습니다.");
                break;
            }

            // Server로 전송
            future = channel.writeAndFlush(message);

            if ("quit".equals(message)) {
                channel.close();
                break;
            }
        }

        // 종료되기 전 모든 메시지가 flush 될 떄까지 기다림
        if (future != null) {
            future.sync();
        }
    }

    public static void main(String[] args) {
        TelnetClient client = new TelnetClient();

        try {
            client.connect();
            client.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            client.close();
        }
    }

}