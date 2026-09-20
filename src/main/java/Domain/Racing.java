package Domain;

import java.util.List;
import java.util.Map;

public final class Racing {

    private final RacingCars cars;
    private final Round round;
    private final AdvanceDecider advanceDecider;
    private RacingStatus status;
    private int completedRounds;

    private Racing(
            List<Name> names,
            Round round,
            AdvanceDecider advanceDecider
    ) {
        requireArguments(names, round, advanceDecider);
        this.cars = new RacingCars(names);
        this.round = round;
        this.advanceDecider = advanceDecider;
        this.status = RacingStatus.READY;
    }

    public static Racing ready(
            List<Name> names,
            Round round,
            AdvanceDecider advanceDecider
    ) {
        return new Racing(names, round, advanceDecider);
    }

    public void start() {
        requireReady();
        status = RacingStatus.RACING;
    }

    public boolean advanceIfPossible() {
        var isRacing = status == RacingStatus.RACING;
        var hasRemainingRounds = completedRounds < round.value();
        var canAdvance = isRacing && hasRemainingRounds;
        if (!canAdvance) {
            return false;
        }

        cars.advance(advanceDecider);
        completedRounds++;
        finishIfCompleted();

        return true;
    }

    public Map<Name, Position> positions() {
        return cars.positions();
    }

    public List<Name> winners() {
        requireFinished();
        return cars.leaders();
    }

    public int completedRounds() {
        return completedRounds;
    }

    private static void requireArguments(
            List<Name> names,
            Round round,
            AdvanceDecider advanceDecider
    ) {
        if (names == null) {
            throw new IllegalArgumentException("경주 참가 자동차 이름 목록은 null일 수 없습니다.");
        }
        if (round == null) {
            throw new IllegalArgumentException("전체 경주 횟수는 null일 수 없습니다.");
        }
        if (advanceDecider == null) {
            throw new IllegalArgumentException("전진 여부 판단 정책은 null일 수 없습니다.");
        }
    }

    private void finishIfCompleted() {
        var isCompleted = completedRounds == round.value();
        if (isCompleted) {
            status = RacingStatus.FINISHED;
        }
    }

    private void requireReady() {
        if (status != RacingStatus.READY) {
            throw new IllegalStateException("준비 상태의 경주만 시작할 수 있습니다.");
        }
    }

    private void requireFinished() {
        if (status != RacingStatus.FINISHED) {
            throw new IllegalStateException("종료된 경주에서만 우승자를 조회할 수 있습니다.");
        }
    }
}
