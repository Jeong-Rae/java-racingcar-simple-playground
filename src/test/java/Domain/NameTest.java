package Domain;

import static Common.ExceptionAssertions.assertDoesNotThrowForEach;
import static Common.ExceptionAssertions.assertThrowsForEach;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Name")
class NameTest {

    @Nested
    @DisplayName("이름을 생성할 때")
    class Creation {

        @Test
        @DisplayName("한글과 영문을 입력하면, 입력값을 보존합니다")
        void preservesAllowedCharacters() {
            var values = List.of("pobi", "포비", "po비");

            values.forEach(value -> assertThat(new Name(value).value()).isEqualTo(value));
        }

        @Test
        @DisplayName("이름 길이가 4자 또는 5자이면, 이름을 생성합니다")
        void acceptsLengthBoundary() {
            var values = List.of("pobi", "pobii");

            assertDoesNotThrowForEach(values, Name::new);
        }

        @Test
        @DisplayName("이름 길이가 6자이면, IllegalArgumentException을 던집니다")
        void rejectsLengthOverBoundary() {
            assertThrowsForEach(
                    List.of("pobiii"),
                    IllegalArgumentException.class,
                    Name::new
            );
        }

        @Test
        @DisplayName("이름이 비어 있거나 공백이면, IllegalArgumentException을 던집니다")
        void rejectsBlankValues() {
            var values = List.of("", " ", "\t");

            assertThrowsForEach(values, IllegalArgumentException.class, Name::new);
        }

        @Test
        @DisplayName("한글과 영문 외의 문자를 포함하면, IllegalArgumentException을 던집니다")
        void rejectsUnsupportedCharacters() {
            var values = List.of("pobi1", "포비!", "po-bi", "포 비", "포비🙂");

            assertThrowsForEach(values, IllegalArgumentException.class, Name::new);
        }
    }
}
