package View;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import Common.ConsoleWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RacingResultViewTest {

    @Nested
    class 경주_결과_출력_화면을_생성할_때 {

        @Test
        void 콘솔_출력기가_null이면_예외를_발생시킵니다() {
            ConsoleWriter writer = null;
            ThrowingCallable executable = () -> new RacingResultView(writer);

            assertThatThrownBy(executable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("콘솔 출력기(ConsoleWriter)는 null일 수 없습니다.");
        }
    }

    @Nested
    class 라운드_결과를_출력할_때 {

        @Test
        void 라운드와_자동차별_위치를_입력하면_진행_결과를_출력합니다() {
            var output = new StringWriter();
            var view = new RacingResultView(ConsoleWriter.string(output));
            var positions = positions();
            var expectedOutput = String.join(
                    System.lineSeparator(),
                    "라운드 2/5",
                    "라이언: ■■",
                    "무지: ■■■",
                    "춘식: ",
                    ""
            );

            view.printRound(2, 5, positions);
            var actualOutput = output.toString();

            assertThat(actualOutput).isEqualTo(expectedOutput);
        }

        @Test
        void 자동차_위치_정보가_null이면_예외를_발생시킵니다() {
            var output = new StringWriter();
            var view = new RacingResultView(ConsoleWriter.string(output));
            Map<String, Integer> positions = null;
            ThrowingCallable executable = () -> view.printRound(1, 1, positions);

            assertThatThrownBy(executable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("자동차 위치 정보는 null일 수 없습니다.");
        }
    }

    private static Map<String, Integer> positions() {
        var positions = new LinkedHashMap<String, Integer>();
        positions.put("라이언", 2);
        positions.put("무지", 3);
        positions.put("춘식", 0);

        return positions;
    }
}
