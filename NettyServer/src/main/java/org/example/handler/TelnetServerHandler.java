package org.example.handler;

import io.netty.channel.*;
import org.example.message.ResponseGenerator;


public class TelnetServerHandler extends SimpleChannelInboundHandler<String> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("채널활성화");
        String welcomeMsg = ResponseGenerator.makeHello();
        System.out.println("전송 메시지: " + welcomeMsg);
        ctx.writeAndFlush(welcomeMsg);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        ResponseGenerator generator = new ResponseGenerator(s);
        String response = generator.response();
        System.out.println("응답메시지: " + response);
        ChannelFuture future = ctx.writeAndFlush(response);

        if(generator.isClose()) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("예외 발생: " + cause.getLocalizedMessage());
        ctx.close();
    }
}
