package Domain;

import static Common.ExceptionAssertions.assertDoesNotThrowForEach;
import static Common.ExceptionAssertions.assertThrowsForEach;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Position")
class PositionTest {

    @Nested
    @DisplayName("위치를 생성할 때")
    class Creation {

        @Test
        @DisplayName("0, 1, 99, 100을 입력하면, 위치를 생성합니다")
        void acceptsBoundaryValues() {
            var values = List.of(0, 1, 99, 100);

            assertDoesNotThrowForEach(values, Position::new);
        }

        @Test
        @DisplayName("-1 또는 101을 입력하면, IllegalArgumentException을 던집니다")
        void rejectsOutsideBoundary() {
            var values = List.of(-1, 101);

            assertThrowsForEach(values, IllegalArgumentException.class, Position::new);
        }
    }

    @Nested
    @DisplayName("시작 위치를 만들 때")
    class Start {

        @Test
        @DisplayName("start를 호출하면, ZERO를 반환합니다")
        void returnsZero() {
            assertThat(Position.start()).isEqualTo(Position.ZERO);
        }
    }

    @Nested
    @DisplayName("위치를 전진할 때")
    class Advance {

        @Test
        @DisplayName("0에서 advance를 호출하면, 위치가 1이 됩니다")
        void advancesOnePosition() {
            var position = Position.start().advance();

            assertThat(position).isEqualTo(new Position(1));
        }
    }
}
