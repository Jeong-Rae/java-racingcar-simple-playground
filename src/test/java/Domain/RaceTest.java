package Domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RaceTest {

    @Nested
    class 경주를_실행할_때 {

        @ParameterizedTest
        @MethodSource("Domain.RaceTest#경주_결과_케이스")
        void 라운드와_전진_판단이_주어지면_가장_앞선_자동차들이_우승한다(
                int round,
                List<List<Boolean>> decisions,
                List<String> expectedNames
        ) {
            var race = new Race(racingCars(decisions));

            race.run(new Round(round));

            assertThat(race.winners())
                    .extracting(car -> car.name().value())
                    .containsExactlyInAnyOrderElementsOf(expectedNames);
        }
    }

    static Stream<Arguments> 경주_결과_케이스() {
        return Stream.of(
                arguments(
                        3,
                        List.of(
                                List.of(true, true, true),
                                List.of(true, false, false),
                                List.of(false, false, false)
                        ),
                        List.of("pobi")
                ),
                arguments(
                        3,
                        List.of(
                                List.of(true, false, true),
                                List.of(true, true, false),
                                List.of(false, false, false)
                        ),
                        List.of("pobi", "crong")
                )
        );
    }

    private static RacingCars racingCars(List<List<Boolean>> decisions) {
        var names = List.of("pobi", "crong", "honux");
        var cars = IntStream.range(0, decisions.size())
                .mapToObj(index -> racingCar(names.get(index), decisions.get(index)))
                .toList();

        return new RacingCars(cars);
    }

    private static RacingCar racingCar(String name, List<Boolean> decisions) {
        return new RacingCar(new Name(name), new TestAdvanceDecider(decisions));
    }
}
