package View;

import static org.assertj.core.api.Assertions.assertThat;

import Common.ConsoleReader;
import Common.ConsoleWriter;
import java.io.StringWriter;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RacingFormViewTest {

    @Nested
    class 자동차_이름을_입력받을_때 {

        @Test
        void 쉼표로_구분된_이름을_입력하면_문자열_목록을_반환합니다() {
            var input = "라이언, 무지, 춘식";
            var expectedNames = List.of("라이언", "무지", "춘식");
            var output = new StringWriter();
            var view = view(input, output);

            var actualNames = view.readCarNames();

            assertThat(actualNames).isEqualTo(expectedNames);
        }

        @Test
        void 이름을_입력받으면_자동차_이름_입력_문구를_출력합니다() {
            var input = "라이언";
            var expectedOutput = "경주할 자동차 이름을 입력하세요.(이름은 쉼표(,) 기준으로 구분)"
                    + System.lineSeparator();
            var output = new StringWriter();
            var view = view(input, output);

            view.readCarNames();
            var actualOutput = output.toString();

            assertThat(actualOutput).isEqualTo(expectedOutput);
        }
    }

    @Nested
    class 레이싱_횟수를_입력받을_때 {

        @Test
        void 숫자를_입력하면_int로_반환합니다() {
            var input = "5";
            var expectedRaceCount = 5;
            var output = new StringWriter();
            var view = view(input, output);

            var actualRaceCount = view.readRaceCount();

            assertThat(actualRaceCount).isEqualTo(expectedRaceCount);
        }

        @Test
        void 레이싱_횟수를_입력받으면_횟수_입력_문구를_출력합니다() {
            var input = "5";
            var expectedOutput = "시도할 횟수는 몇 회인가요?" + System.lineSeparator();
            var output = new StringWriter();
            var view = view(input, output);

            view.readRaceCount();
            var actualOutput = output.toString();

            assertThat(actualOutput).isEqualTo(expectedOutput);
        }
    }

    private static RacingFormView view(String input, StringWriter output) {
        var reader = ConsoleReader.string(input);
        var writer = ConsoleWriter.string(output);

        return new RacingFormView(reader, writer);
    }
}
