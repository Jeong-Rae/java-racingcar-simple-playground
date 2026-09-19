package Domain;

import java.util.List;
import java.util.Objects;

public final class RacingCars {

    private final List<RacingCar> cars;

    public RacingCars(List<RacingCar> cars) {
        this.cars = List.copyOf(Objects.requireNonNull(cars, "자동차 목록은 null일 수 없습니다."));
        if (this.cars.isEmpty()) {
            throw new IllegalArgumentException("자동차는 한 대 이상이어야 합니다.");
        }
    }

    public void advance() {
        cars.forEach(RacingCar::advance);
    }

    public List<RacingCar> leaders() {
        var maxPosition = maxPosition();

        return cars.stream()
                .filter(car -> car.isAt(maxPosition))
                .toList();
    }

    private Position maxPosition() {
        return cars.stream()
                .map(RacingCar::position)
                .max(Position::compareTo)
                .orElseThrow(() -> new IllegalStateException("경주할 자동차가 존재하지 않습니다."));
    }
}
