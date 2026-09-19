package Domain;

import static Common.ExceptionAssertions.assertDoesNotThrowForEach;
import static Common.ExceptionAssertions.assertThrowsForEach;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class NameTest {

    @Nested
    class 이름을_생성할_때 {

        @Test
        void 한글과_영문을_입력하면_입력값을_보존한다() {
            var values = List.of("pobi", "포비", "po비");

            values.forEach(value -> assertThat(new Name(value).value()).isEqualTo(value));
        }

        @Test
        void 이름_길이가_4자_또는_5자이면_이름을_생성한다() {
            var values = List.of("pobi", "pobii");

            assertDoesNotThrowForEach(values, Name::new);
        }

        @Test
        void 이름_길이가_6자이면_IllegalArgumentException을_던진다() {
            assertThrowsForEach(
                    List.of("pobiii"),
                    IllegalArgumentException.class,
                    Name::new
            );
        }

        @Test
        void 이름이_비어_있거나_공백이면_IllegalArgumentException을_던진다() {
            var values = List.of("", " ", "\t");

            assertThrowsForEach(values, IllegalArgumentException.class, Name::new);
        }

        @Test
        void 한글과_영문_외의_문자를_포함하면_IllegalArgumentException을_던진다() {
            var values = List.of("pobi1", "포비!", "po-bi", "포 비", "포비🙂");

            assertThrowsForEach(values, IllegalArgumentException.class, Name::new);
        }
    }
}
