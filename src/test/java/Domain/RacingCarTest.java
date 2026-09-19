package Domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("RacingCar")
class RacingCarTest {

    @Nested
    @DisplayName("advance를 호출할 때")
    class Advance {

        @Test
        @DisplayName("AdvanceDecider가 true이면, 위치가 1 증가합니다")
        void advancesWhenDeciderAllows() {
            var car = new RacingCar(new Name("pobi"), new TestAdvanceDecider(true));

            car.advance();

            assertThat(car.position()).isEqualTo(new Position(1));
        }

        @Test
        @DisplayName("AdvanceDecider가 false이면, 위치를 유지합니다")
        void staysWhenDeciderRejects() {
            var car = new RacingCar(new Name("pobi"), new TestAdvanceDecider(false));

            car.advance();

            assertThat(car.position()).isEqualTo(Position.ZERO);
        }

        @Test
        @DisplayName("판단 결과가 true, false, true이면, 최종 위치가 2가 됩니다")
        void advancesAccordingToDecisions() {
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
