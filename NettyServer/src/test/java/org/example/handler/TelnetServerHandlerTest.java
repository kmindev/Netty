package org.example.handler;

import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TelnetServerHandler 테스트")
class TelnetServerHandlerTest {

    @Test
    void test1() {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("환영합니다. ")
                    .append(InetAddress.getLocalHost().getHostName())
                    .append("에 접속하셨습니다.\r\n")
                    .append("현재 시간은 ")
                    .append(new Date().toString())
                    .append(" 입니다.\r\n");
        } catch (UnknownHostException e) {
            fail();
            e.printStackTrace();
        }

        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new TelnetServerHandler());

        String expected = (String) embeddedChannel.readOutbound();

        assertThat(expected).isEqualTo(sb.toString());

        embeddedChannel.finish();
    }

    @Test
    void test2() {
        String request = "hello";
        String expected = "입력하신 명령어가 '" + request + "'입니까?\r\n";

        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new TelnetServerHandler());
        embeddedChannel.writeInbound(request);

        embeddedChannel.readOutbound(); // active 이벤트의 메시지를 버린다.
        String channelReadMsg = (String) embeddedChannel.readOutbound();

        assertThat(expected).isEqualTo(channelReadMsg);

        embeddedChannel.finish();
    }

    @Test
    void test3() {
        String request = "bye";
        String expected = "좋은 하루 보내세요.\r\n";

        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new TelnetServerHandler());
        embeddedChannel.writeInbound(request);

        embeddedChannel.readOutbound(); // active 이벤트의 메시지를 버린다.
        String channelReadMsg = (String) embeddedChannel.readOutbound();

        assertThat(expected).isEqualTo(channelReadMsg);

        embeddedChannel.finish();
    }

}