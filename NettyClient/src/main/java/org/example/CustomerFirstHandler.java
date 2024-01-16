package org.example;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Scanner;

public class CustomerFirstHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("채널이 활성화 되었습니다.");
        ctx.writeAndFlush("안녕하세요.");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String data = (String) msg;
        System.out.println("first: " + data);

        ctx.writeAndFlush(menuInput());

        ctx.pipeline().remove(this)
                .addLast(new CustomerSecondHandler());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("예외:" + cause.getLocalizedMessage());
        ctx.close();
    }

    private String menuInput() {
        Scanner sc = new Scanner(System.in);
        String sendData = "";

        while (!sendData.contains("1") && !sendData.contains("2") && !sendData.contains("3")) {
            System.out.println("메뉴를 선택해주세요. (1, 2, 3): ");
            sendData = sc.nextLine();
        }

        sc.close();

        return sendData;
    }
}
