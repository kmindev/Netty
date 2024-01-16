package org.example;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class RestaurantThirdHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String data = (String) msg;
        System.out.println("thrid: "+ data);

        if(data.contains("돈가스")) {
            ctx.writeAndFlush("주문하신 돈가스가 나왔습니다.");
        } else if (data.contains("김밥")) {
            ctx.writeAndFlush("주문하신 김밥이 나왔습니다.");
        } else if(data.contains("떡볶이")) {
            ctx.writeAndFlush("주문하신 떡볶이가 나왔습니다.");
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("예외 발생: " + cause.getLocalizedMessage());
        ctx.close();
    }

}
