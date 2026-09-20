package Domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class NameTest {

    @Nested
    class 이름을_생성할_때 {

        @ParameterizedTest
        @ValueSource(strings = {
                "라이언",
                "RYAN",
                "무지",
                "MUZI",
                "춘식"
        })
        void 한글과_영문_이름을_입력하면_입력값을_보존합니다(String value) {
            var expectedValue = value;

            var actualValue = Name.of(value).value();

            assertThat(actualValue).isEqualTo(expectedValue);
        }

        @ParameterizedTest
        @ValueSource(ints = {
                1,
                2,
                4,
                5
        })
        void 이름_길이가_1자부터_5자이면_이름을_생성합니다(int length) {
            var value = "A".repeat(length);
            ThrowingCallable executable = () -> Name.of(value);

            assertThatCode(executable)
                    .doesNotThrowAnyException();
        }

        @ParameterizedTest
        @ValueSource(ints = {
                0,
                6
        })
        void 이름_길이가_1자부터_5자_범위를_벗어나면_예외를_발생시킵니다(int length) {
            var value = "A".repeat(length);
            ThrowingCallable executable = () -> Name.of(value);

            assertThatThrownBy(executable)
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 이름이_null이면_예외를_발생시킵니다() {
            String value = null;
            ThrowingCallable executable = () -> Name.of(value);

            assertThatThrownBy(executable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("자동차 이름은 null일 수 없습니다.");
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "",
                " ",
                "\t"
        })
        void 이름이_비어_있거나_공백이면_예외를_발생시킵니다(String value) {
            ThrowingCallable executable = () -> Name.of(value);

            assertThatThrownBy(executable)
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "RYAN1",
                "무 지",
                "춘식!"
        })
        void 한글과_영문_외의_문자를_포함하면_예외를_발생시킵니다(String value) {
            ThrowingCallable executable = () -> Name.of(value);

            assertThatThrownBy(executable)
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
