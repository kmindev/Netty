package org.example;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class RestaurantFirstHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("손님이 입장했습니다. " + ctx.channel().remoteAddress());

        String welcomeMsg = "안녕하세요. 메뉴를 선택해주세요.";
        String menu = "메뉴: 1. 돈가스    2. 김밥    3. 떡볶이 ";
        ctx.write(welcomeMsg);
        ctx.write(menu);
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String data = (String) msg;
        System.out.println("first: " + data);

        ctx.pipeline().remove(this)  // 파이프라인에서 현재 이벤트 핸들러 제거
                .addLast(new RestaurantSecondHandler()); // 새로운 이벤트 핸들러 추가
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("예외 발생: " + cause.getLocalizedMessage());
        ctx.close();
    }
}
