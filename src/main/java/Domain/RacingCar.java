package Domain;

import java.util.Objects;

public final class RacingCar {

    private final Name name;
    private final AdvanceDecider advanceDecider;
    private Position position;

    public RacingCar(Name name, AdvanceDecider advanceDecider) {
        this.name = Objects.requireNonNull(name, "자동차 이름은 null일 수 없습니다.");
        this.advanceDecider = Objects.requireNonNull(
                advanceDecider,
                "전진 여부를 판단하는 정책은 null일 수 없습니다."
        );
        this.position = Position.start();
    }

    public Name name() {
        return name;
    }

    public Position position() {
        return position;
    }

    public void advance() {
        if (!advanceDecider.shouldAdvance()) {
            return;
        }
        position = position.advance();
    }

    public boolean isAt(Position position) {
        return this.position.equals(position);
    }
}
