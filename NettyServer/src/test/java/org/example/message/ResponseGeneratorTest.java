package org.example.message;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Response 테스트")
public class ResponseGeneratorTest {

    @DisplayName("cmd 테슽")
    @Test
    void givenStringHi_whenCreateResponseGenerator_thenReturnsResponse() {
        String request = "hi";

        ResponseGenerator generator = new ResponseGenerator(request);
        assertThat(generator).isNotNull();

        assertThat(generator.response()).isNotNull();
        assertThat(generator.response()).isEqualTo("입력하신 명령어가 'hi'입니까?\r\n");

        assertThat(generator.isClose()).isFalse();
    }

    @DisplayName("bye 테스트")
    @Test
    void givenStringBye_whenCreateResponseGenerator_thenReturnsResponse() {
        String request = "bye";

        ResponseGenerator generator = new ResponseGenerator(request);
        assertThat(generator).isNotNull();

        assertThat(generator.response()).isNotNull();
        assertThat(generator.response()).isEqualTo("좋은 하루 보내세요.\r\n");

        assertThat(generator.isClose()).isTrue();
    }
}
