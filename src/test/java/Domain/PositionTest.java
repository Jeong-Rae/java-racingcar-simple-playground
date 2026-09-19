package Domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PositionTest {

    @Nested
    class 위치를_생성할_때 {

        @ParameterizedTest
        @CsvSource({
                "0",
                "1",
                "99",
                "100"
        })
        void 값이_0부터_100_사이면_위치를_생성합니다(int value) {
            ThrowingCallable executable = () -> Position.of(value);

            assertThatCode(executable)
                    .doesNotThrowAnyException();
        }

        @ParameterizedTest
        @CsvSource({
                "-1",
                "101"
        })
        void 값이_0부터_100_사이가_아니면_예외를_발생시킵니다(int value) {
            ThrowingCallable executable = () -> Position.of(value);

            assertThatThrownBy(executable)
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 시작_위치를_만들_때 {

        @Test
        void start를_호출하면_ZERO를_반환합니다() {
            var expectedPosition = Position.ZERO;

            var actualPosition = Position.start();

            assertThat(actualPosition).isEqualTo(expectedPosition);
        }
    }

    @Nested
    class 위치를_전진할_때 {

        @Test
        void 영에서_advance를_호출하면_위치가_1이_됩니다() {
            var position = Position.start();
            var expectedPosition = Position.of(1);

            var actualPosition = position.advance();

            assertThat(actualPosition).isEqualTo(expectedPosition);
        }

        @Test
        void 위치가_100이면_advance를_호출할_때_예외를_발생시킵니다() {
            var position = Position.of(100);
            ThrowingCallable executable = position::advance;

            assertThatThrownBy(executable)
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
