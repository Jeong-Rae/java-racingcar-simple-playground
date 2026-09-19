package Domain;

import java.util.List;
import java.util.Objects;

public final class Race {

    private final RacingCars cars;

    public Race(RacingCars cars) {
        this.cars = Objects.requireNonNull(cars, "경주할 자동차 목록은 null일 수 없습니다.");
    }

    public void run(Round round) {
        for (var current = 0; current < round.value(); current++) {
            cars.advance();
        }
    }

    public List<RacingCar> winners() {
        return cars.leaders();
    }
}
