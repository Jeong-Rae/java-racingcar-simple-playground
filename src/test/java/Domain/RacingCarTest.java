package Domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class RacingCarTest {

    @Test
    void 랜덤_값이_4_이상이면_전진한다() {
        var car = new RacingCar(new Name("pobi"));

        car.move(4);

        assertThat(car.position()).isEqualTo(new Position(1));
    }

    @Test
    void 랜덤_값이_4_미만이면_멈춘다() {
        var car = new RacingCar(new Name("pobi"));

        car.move(3);

        assertThat(car.position()).isEqualTo(Position.ZERO);
    }

    @Test
    void 랜덤_값은_0부터_9_사이여야_한다() {
        var car = new RacingCar(new Name("pobi"));

        assertThatThrownBy(() -> car.move(10))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
