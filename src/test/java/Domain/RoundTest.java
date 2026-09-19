package Domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class RoundTest {

    @Nested
    class 라운드를_생성할_때 {

        @ParameterizedTest
        @CsvSource({
                "1",
                "2"
        })
        void 값이_1_이상이면_라운드를_생성한다(int value) {
            assertThatCode(() -> new Round(value))
                    .doesNotThrowAnyException();
        }

        @ParameterizedTest
        @CsvSource({
                "-1",
                "0"
        })
        void 값이_1보다_작으면_IllegalArgumentException을_던진다(int value) {
            assertThatThrownBy(() -> new Round(value))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
