package Controller;

import Domain.AdvanceDecider;
import Domain.Name;
import Domain.Racing;
import Domain.RacingCars;
import Domain.RacingStatus;
import Domain.Round;
import View.RacingFormView;
import java.util.List;

public final class RacingController {

    private final RacingFormView formView;
    private final AdvanceDecider advanceDecider;

    public RacingController(RacingFormView formView, AdvanceDecider advanceDecider) {
        validate(formView, advanceDecider);
        this.formView = formView;
        this.advanceDecider = advanceDecider;
    }

    public void run() {
        var names = formView.readCarNames();
        var raceCount = formView.readRaceCount();
        var racing = Racing.ready(racingCars(names), Round.of(raceCount), advanceDecider);

        racing.start();
        while (racing.status() == RacingStatus.RACING) {
            racing.advance();
        }
    }

    private static void validate(RacingFormView formView, AdvanceDecider advanceDecider) {
        if (formView == null) {
            throw new IllegalArgumentException(
                    "경주 정보 입력 화면(RacingFormView)는 null일 수 없습니다."
            );
        }
        if (advanceDecider == null) {
            throw new IllegalArgumentException(
                    "전진 여부 판단 정책(AdvanceDecider)은 null일 수 없습니다."
            );
        }
    }

    private RacingCars racingCars(List<String> names) {
        var participants = names.stream()
                .map(Name::of)
                .toList();

        return new RacingCars(participants);
    }
}
