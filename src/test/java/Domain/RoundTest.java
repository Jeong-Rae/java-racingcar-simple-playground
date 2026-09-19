package Domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class RoundTest {

    @Nested
    class 라운드를_생성할_때 {

        @ParameterizedTest
        @CsvSource({
                "1",
                "2",
                "99",
                "100"
        })
        void 값이_1부터_100_사이면_라운드를_생성합니다(int value) {
            ThrowingCallable executable = () -> Round.of(value);

            assertThatCode(executable)
                    .doesNotThrowAnyException();
        }

        @ParameterizedTest
        @CsvSource({
                "-1",
                "0",
                "101"
        })
        void 값이_1부터_100_사이가_아니면_예외를_발생시킵니다(int value) {
            ThrowingCallable executable = () -> Round.of(value);

            assertThatThrownBy(executable)
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
