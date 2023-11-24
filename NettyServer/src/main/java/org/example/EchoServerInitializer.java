package org.example;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class EchoServerInitializer extends ChannelInitializer<SocketChannel> {

    // 파이프라인을 만들어준다.
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        System.out.println(socketChannel.remoteAddress() +"  클라이언트가 접속했습니다.");
        ChannelPipeline pipeline = socketChannel.pipeline();

        pipeline.addLast(new LineBasedFrameDecoder(65536));
        pipeline.addLast(new StringDecoder());
        pipeline.addLast(new StringEncoder());
        pipeline.addLast(new EchoServerHandler()); // 메시지 수신 핸들러

    }
}
