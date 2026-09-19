package Domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;
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
                "pobi",
                "포비",
                "po비"
        })
        void 한글과_영문을_입력하면_입력값을_보존합니다(String value) {
            assertThat(new Name(value).value()).isEqualTo(value);
        }

        @ParameterizedTest
        @CsvSource({
                "pobi",
                "pobii"
        })
        void 이름_길이가_5자_이하이면_이름을_생성합니다(String value) {
            assertThatCode(() -> new Name(value))
                    .doesNotThrowAnyException();
        }

        @ParameterizedTest
        @CsvSource({
                "pobiii",
                "abcdef"
        })
        void 이름_길이가_5자를_초과하면_IllegalArgumentException을_던집니다(String value) {
            assertThatThrownBy(() -> new Name(value))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @MethodSource("Domain.NameTest#비어_있거나_공백인_이름")
        void 이름이_비어_있거나_공백이면_IllegalArgumentException을_던집니다(String value) {
            assertThatThrownBy(() -> new Name(value))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @MethodSource("Domain.NameTest#지원하지_않는_문자가_포함된_이름")
        void 한글과_영문_외의_문자를_포함하면_IllegalArgumentException을_던집니다(String value) {
            assertThatThrownBy(() -> new Name(value))
                    .isInstanceOf(IllegalArgumentException.class);
        }
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
                arguments("pobi1"),
                arguments("포비!"),
                arguments("po-bi"),
                arguments("포 비"),
                arguments("포비🙂")
        );
    }
}
