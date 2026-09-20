package Common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ConsoleReaderTest {

    @Nested
    class 문자열_입력기를_생성할_때 {

        @Test
        void 입력_문자열이_null이면_예외를_발생시킵니다() {
            String input = null;
            ThrowingCallable executable = () -> ConsoleReader.string(input);

            assertThatThrownBy(executable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("문자열 입력은 null일 수 없습니다.");
        }
    }

    @Nested
    class 문자열_입력을_읽을_때 {

        @Test
        void 여러_줄을_입력하면_입력된_순서대로_한_줄씩_반환합니다() {
            var input = "hello\nworld";
            var expectedFirstLine = "hello";
            var expectedSecondLine = "world";
            var reader = ConsoleReader.string(input);

            var actualFirstLine = reader.readLine();
            var actualSecondLine = reader.readLine();

            assertThat(actualFirstLine).isEqualTo(expectedFirstLine);
            assertThat(actualSecondLine).isEqualTo(expectedSecondLine);
        }
    }
}
