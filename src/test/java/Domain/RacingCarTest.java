package Domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RacingCarTest {

    @Nested
    class advance를_호출할_때 {

        @Test
        void AdvanceDecider가_true이면_위치가_1_증가한다() {
            var car = new RacingCar(new Name("pobi"), new TestAdvanceDecider(true));

            car.advance();

            assertThat(car.position()).isEqualTo(new Position(1));
        }

        @Test
        void AdvanceDecider가_false이면_위치를_유지한다() {
            var car = new RacingCar(new Name("pobi"), new TestAdvanceDecider(false));

            car.advance();

            assertThat(car.position()).isEqualTo(Position.ZERO);
        }

        @Test
        void 판단_결과가_true_false_true이면_최종_위치가_2가_된다() {
            var car = new RacingCar(
                    new Name("pobi"),
                    new TestAdvanceDecider(List.of(true, false, true))
            );

            car.advance();
            car.advance();
            car.advance();

            assertThat(car.position()).isEqualTo(new Position(2));
        }
    }
}
