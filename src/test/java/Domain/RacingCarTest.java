package Domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class RacingCarTest {

    @Test
    void 전진을_허용하면_한_칸_전진한다() {
        var car = new RacingCar(new Name("pobi"), new TestAdvanceDecider(true));

        car.advance();

        assertThat(car.position()).isEqualTo(new Position(1));
    }

    @Test
    void 전진을_거절하면_현재_위치를_유지한다() {
        var car = new RacingCar(new Name("pobi"), new TestAdvanceDecider(false));

        car.advance();

        assertThat(car.position()).isEqualTo(Position.ZERO);
    }

    @Test
    void 주어진_판단_결과를_순서대로_사용한다() {
        var decider = new TestAdvanceDecider(List.of(true, false, true));
        var car = new RacingCar(new Name("pobi"), decider);

        car.advance();
        car.advance();
        car.advance();

        assertThat(car.position()).isEqualTo(new Position(2));
    }
}
