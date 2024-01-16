package org.example.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.example.handler.RestaurantFirstHandler;

import java.nio.charset.Charset;

public class RestaurantServerInitializer extends ChannelInitializer<SocketChannel> {

    private final Charset charset = Charset.defaultCharset();
    @Override
    protected void initChannel(SocketChannel socketChannel) {
        socketChannel.pipeline()
                .addLast(new StringDecoder(charset), new StringEncoder(charset))
                .addLast(new RestaurantFirstHandler());
    }
}
