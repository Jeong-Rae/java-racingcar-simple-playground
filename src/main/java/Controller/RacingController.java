package Controller;

import Domain.AdvanceDecider;
import Domain.Name;
import Domain.Race;
import Domain.RacingCar;
import Domain.RacingCars;
import Domain.Round;
import View.RacingFormView;
import java.util.List;
import java.util.Objects;

public final class RacingController {

    private final RacingFormView formView;
    private final AdvanceDecider advanceDecider;

    public RacingController(RacingFormView formView, AdvanceDecider advanceDecider) {
        this.formView = Objects.requireNonNull(
                formView,
                "경주 정보 입력 화면(RacingFormView)는 null일 수 없습니다."
        );
        this.advanceDecider = Objects.requireNonNull(
                advanceDecider,
                "전진 여부 판단 정책(AdvanceDecider)은 null일 수 없습니다."
        );
    }

    public void run() {
        var names = formView.readCarNames();
        var raceCount = formView.readRaceCount();
        var race = new Race(racingCars(names));

        race.run(Round.of(raceCount));
    }

    private RacingCars racingCars(List<String> names) {
        var cars = names.stream()
                .map(this::racingCar)
                .toList();

        return new RacingCars(cars);
    }

    private RacingCar racingCar(String name) {
        return new RacingCar(Name.of(name), advanceDecider);
    }
}
