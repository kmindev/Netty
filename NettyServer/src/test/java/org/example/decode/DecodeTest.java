package org.example.decode;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("decode 테스트")
public class DecodeTest {

    @DisplayName("DelimiterBasedFrameDecoder 테스트")
    @Test
    void givenWriteData_whenDelimiterBasedFrameDecoding_thenReturnDecodingResult () {
        String writeData = "안녕하세요\r\n반갑습니다\r\n";
        String firstResponse = "안녕하세요\r\n";
        String secondResponse = "반갑습니다\r\n";
        DelimiterBasedFrameDecoder decoder = new DelimiterBasedFrameDecoder(8192, false, Delimiters.lineDelimiter());
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(decoder);

        ByteBuf request = Unpooled.wrappedBuffer(writeData.getBytes());
        boolean result = embeddedChannel.writeInbound(request);
        assertThat(result).isTrue();

        ByteBuf response = null;
        response = (ByteBuf) embeddedChannel.readInbound();
        assertThat(firstResponse).isEqualTo(response.toString(Charset.defaultCharset()));

        response = (ByteBuf) embeddedChannel.readInbound();
        assertThat(secondResponse).isEqualTo(response.toString(Charset.defaultCharset()));

        embeddedChannel.finish();
    }
}
