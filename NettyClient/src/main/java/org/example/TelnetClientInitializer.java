package org.example;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.Charset;

public class TelnetClientInitializer extends ChannelInitializer<SocketChannel> {

    private final Charset charset = Charset.defaultCharset();

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

        pipeline.addLast(new StringDecoder(charset))
                .addLast(new StringEncoder(charset))
                .addLast(new TelnetClientHandler());
    }
}
