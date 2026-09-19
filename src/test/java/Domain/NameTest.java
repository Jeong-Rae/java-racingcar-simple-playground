package Domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
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
        @ValueSource(strings = {
                "라이언라",
                "라이언라이"
        })
        void 이름_길이가_4자와_5자이면_이름을_생성합니다(String value) {
            ThrowingCallable executable = () -> Name.of(value);

            assertThatCode(executable)
                    .doesNotThrowAnyException();
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "라이언라이언",
                "CHOONSIK"
        })
        void 이름_길이가_5자를_초과하면_예외를_발생시킵니다(String value) {
            ThrowingCallable executable = () -> Name.of(value);

            assertThatThrownBy(executable)
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @NullSource
        void 이름이_null이면_예외를_발생시킵니다(String value) {
            ThrowingCallable executable = () -> Name.of(value);

            assertThatThrownBy(executable)
                    .isInstanceOf(NullPointerException.class);
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
