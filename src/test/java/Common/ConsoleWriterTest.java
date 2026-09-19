package Common;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.StringWriter;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ConsoleWriterTest {

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
