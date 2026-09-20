package Controller;

import Domain.AdvanceDecider;
import Domain.Name;
import Domain.Racing;
import Domain.RacingCars;
import Domain.RacingStatus;
import Domain.Round;
import View.RacingFormView;
import View.RacingResultView;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class RacingController {

    private final RacingFormView formView;
    private final RacingResultView resultView;
    private final AdvanceDecider advanceDecider;

    public RacingController(
            RacingFormView formView,
            RacingResultView resultView,
            AdvanceDecider advanceDecider
    ) {
        requireDependencies(formView, resultView, advanceDecider);
        this.formView = formView;
        this.resultView = resultView;
        this.advanceDecider = advanceDecider;
    }

    public void run() {
        var names = formView.readCarNames();
        var raceCount = formView.readRaceCount();
        var racing = Racing.ready(racingCars(names), Round.of(raceCount), advanceDecider);

        racing.start();
        while (racing.status() == RacingStatus.RACING) {
            racing.advance();
            resultView.printRound(
                    racing.completedRounds(),
                    raceCount,
                    positionsOf(racing)
            );
        }
        resultView.printWinners(winnerNamesOf(racing));
    }

    private static void requireDependencies(
            RacingFormView formView,
            RacingResultView resultView,
            AdvanceDecider advanceDecider
    ) {
        if (formView == null) {
            throw new IllegalArgumentException(
                    "경주 정보 입력 화면(RacingFormView)는 null일 수 없습니다."
            );
        }
        if (resultView == null) {
            throw new IllegalArgumentException(
                    "경주 결과 출력 화면(RacingResultView)는 null일 수 없습니다."
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

    private Map<String, Integer> positionsOf(Racing racing) {
        var positions = new LinkedHashMap<String, Integer>();
        racing.positions().forEach(
                (name, position) -> positions.put(name.value(), position.value())
        );

        return positions;
    }

    private List<String> winnerNamesOf(Racing racing) {
        return racing.winners().stream()
                .map(Name::value)
                .toList();
    }
}
