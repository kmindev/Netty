package org.example;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class RestaurantSecondHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String data = (String) msg;
        System.out.println("second: " + data);

        String orderMenu = menuCheck(data);

        if (orderMenu != null) {
            ctx.pipeline()
                    .addLast(new RestaurantThirdHandler());
            ctx.fireChannelRead(orderMenu + "를 주문했습니다."); // 다음 이벤트 핸들러에 channelRead 이벤트를 발생시킨다.
        } else {
            System.out.println("메뉴에 없는 주문입니다.");
            ctx.writeAndFlush("메뉴에 없는 주문입니다.");
            ctx.pipeline().remove(this)
                    .addLast(new RestaurantFirstHandler());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("예외 발생: " + cause.getLocalizedMessage());
        ctx.close();
    }

    private String menuCheck(String data) {
        String orderMenu = null;

        if (data.contains("1")) {
            orderMenu = "돈가스";
        } else if (data.contains("2")) {
            orderMenu = "김밥";
        } else if (data.contains("3")) {
            orderMenu = "떡볶이";
        }
        return orderMenu;
    }
}
