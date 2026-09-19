package Domain;

import java.util.Objects;

public final class RacingCar {

    private static final int MIN_RANDOM_VALUE = 0;
    private static final int MAX_RANDOM_VALUE = 9;
    private static final int MOVE_THRESHOLD = 4;

    private final Name name;
    private Position position;

    public RacingCar(Name name) {
        this.name = Objects.requireNonNull(name, "자동차 이름은 null일 수 없습니다.");
        this.position = Position.start();
    }

    public Name name() {
        return name;
    }

    public Position position() {
        return position;
    }

    public void move(int randomValue) {
        validateRandomValue(randomValue);
        if (randomValue >= MOVE_THRESHOLD) {
            position = position.move();
        }
    }

    private void validateRandomValue(int randomValue) {
        if (randomValue < MIN_RANDOM_VALUE || randomValue > MAX_RANDOM_VALUE) {
            throw new IllegalArgumentException("랜덤 값은 0부터 9 사이여야 합니다.");
        }
    }
}
