package Domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

class NameTest {

    @Nested
    class 이름을_생성할_때 {

        @ParameterizedTest
        @CsvSource({
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
        @CsvSource({
                "라이언",
                "RYAN",
                "무지",
                "MUZI",
                "춘식"
        })
        void 이름_길이가_5자_이하이면_이름을_생성합니다(String value) {
            ThrowingCallable executable = () -> Name.of(value);

            assertThatCode(executable)
                    .doesNotThrowAnyException();
        }

        @ParameterizedTest
        @MethodSource("Domain.NameTest#길이가_5자를_초과하는_이름")
        void 이름_길이가_5자를_초과하면_예외를_발생시킵니다(String value) {
            ThrowingCallable executable = () -> Name.of(value);

            assertThatThrownBy(executable)
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @MethodSource("Domain.NameTest#비어_있거나_공백인_이름")
        void 이름이_비어_있거나_공백이면_예외를_발생시킵니다(String value) {
            ThrowingCallable executable = () -> Name.of(value);

            assertThatThrownBy(executable)
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @MethodSource("Domain.NameTest#지원하지_않는_문자가_포함된_이름")
        void 한글과_영문_외의_문자를_포함하면_예외를_발생시킵니다(String value) {
            ThrowingCallable executable = () -> Name.of(value);

            assertThatThrownBy(executable)
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    static Stream<Arguments> 길이가_5자를_초과하는_이름() {
        return Stream.of(
                arguments("CHOONSIK"),
                arguments("라이언라이언")
        );
    }

    static Stream<Arguments> 비어_있거나_공백인_이름() {
        return Stream.of(
                arguments(""),
                arguments(" "),
                arguments("\t")
        );
    }

    static Stream<Arguments> 지원하지_않는_문자가_포함된_이름() {
        return Stream.of(
                arguments("RYAN1"),
                arguments("무 지"),
                arguments("춘식!")
        );
    }
}
