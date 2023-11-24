package org.example;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String message = (String)msg;

        System.out.println(message);

        Channel channel = ctx.channel();
        channel.writeAndFlush("Resoponse : '" + message + "' received\n");

        if("quit".equals(message)) {
            ctx.close();
        }
    }
}
