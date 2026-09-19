package View;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ConsoleReaderTest {

    @Nested
    class 문자열_입력을_읽을_때 {

        @Test
        void 여러_줄을_입력하면_입력된_순서대로_한_줄씩_반환합니다() {
            var reader = ConsoleReader.string("hello\nworld");

            assertThat(reader.readLine()).isEqualTo("hello");
            assertThat(reader.readLine()).isEqualTo("world");
        }
    }
}
