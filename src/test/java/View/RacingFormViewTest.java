package View;

import static org.assertj.core.api.Assertions.assertThat;

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
            var view = new RacingFormView(ConsoleReader.string(input));

            var actualNames = view.readCarNames();

            assertThat(actualNames).isEqualTo(expectedNames);
        }
    }

    @Nested
    class 레이싱_횟수를_입력받을_때 {

        @Test
        void 숫자를_입력하면_int로_반환합니다() {
            var input = "5";
            var expectedRaceCount = 5;
            var view = new RacingFormView(ConsoleReader.string(input));

            var actualRaceCount = view.readRaceCount();

            assertThat(actualRaceCount).isEqualTo(expectedRaceCount);
        }
    }
}
