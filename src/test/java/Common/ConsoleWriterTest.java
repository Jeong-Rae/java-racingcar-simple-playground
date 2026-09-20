package Common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.StringWriter;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ConsoleWriterTest {

    @Nested
    class 문자열_출력기를_생성할_때 {

        @Test
        void 문자열_출력기가_null이면_예외를_발생시킵니다() {
            StringWriter output = null;
            ThrowingCallable executable = () -> ConsoleWriter.string(output);

            assertThatThrownBy(executable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("문자열 출력기(StringWriter)는 null일 수 없습니다.");
        }
    }

    @Nested
    class 문자열을_출력할_때 {

        @Test
        void 템플릿과_값을_입력하면_값을_치환해_출력합니다() {
            var template = "hello %s, count %d";
            var expectedOutput = "hello world, count 3";
            var output = new StringWriter();
            var writer = ConsoleWriter.string(output);

            writer.write(template, "world", 3);
            var actualOutput = output.toString();

            assertThat(actualOutput).isEqualTo(expectedOutput);
        }

        @Test
        void writeLine을_호출하면_줄바꿈을_포함해_출력합니다() {
            var input = "hello";
            var expectedOutput = "hello" + System.lineSeparator();
            var output = new StringWriter();
            var writer = ConsoleWriter.string(output);

            writer.writeLine(input);
            var actualOutput = output.toString();

            assertThat(actualOutput).isEqualTo(expectedOutput);
        }
    }
}
