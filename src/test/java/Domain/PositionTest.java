package Domain;

import static Common.ExceptionAssertions.assertDoesNotThrowForEach;
import static Common.ExceptionAssertions.assertThrowsForEach;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class PositionTest {

    @Nested
    class 위치를_생성할_때 {

        @Test
        void 경계_안의_값을_입력하면_위치를_생성한다() {
            var values = List.of(0, 1, 99, 100);

            assertDoesNotThrowForEach(values, Position::new);
        }

        @Test
        void 경계를_벗어난_값을_입력하면_IllegalArgumentException을_던진다() {
            var values = List.of(-1, 101);

            assertThrowsForEach(values, IllegalArgumentException.class, Position::new);
        }
    }

    @Nested
    class 시작_위치를_만들_때 {

        @Test
        void start를_호출하면_ZERO를_반환한다() {
            assertThat(Position.start()).isEqualTo(Position.ZERO);
        }
    }

    @Nested
    class 위치를_전진할_때 {

        @Test
        void 영에서_advance를_호출하면_위치가_1이_된다() {
            var position = Position.start().advance();

            assertThat(position).isEqualTo(new Position(1));
        }
    }
}
