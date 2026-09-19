package Domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RaceTest {

    @Nested
    class 경주를_실행할_때 {

        @ParameterizedTest
        @MethodSource("Domain.RaceTest#경주_결과_케이스")
        void 라운드와_전진_판단이_주어지면_가장_앞선_자동차들이_우승합니다(
                int round,
                List<List<Boolean>> decisions,
                List<String> expectedNames
        ) {
            var race = new Race(racingCars(decisions));

            race.run(Round.of(round));
            var actualWinners = race.winners();

            assertThat(actualWinners)
                    .extracting(car -> car.name().value())
                    .containsExactlyInAnyOrderElementsOf(expectedNames);
        }

        @Test
        void 100라운드에서_항상_전진하면_최종_위치가_100이_됩니다() {
            var car = new RacingCar(
                    Name.of("RYAN"),
                    new TestAdvanceDecider(Collections.nCopies(100, true))
            );
            var race = new Race(new RacingCars(List.of(car)));
            var expectedPosition = Position.of(100);

            race.run(Round.of(100));
            var actualPosition = car.position();

            assertThat(actualPosition).isEqualTo(expectedPosition);
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
                        List.of("RYAN")
                ),
                arguments(
                        3,
                        List.of(
                                List.of(true, false, true),
                                List.of(true, true, false),
                                List.of(false, false, false)
                        ),
                        List.of("RYAN", "MUZI")
                )
        );
    }

    private static RacingCars racingCars(List<List<Boolean>> decisions) {
        var names = List.of("RYAN", "MUZI", "춘식");
        var cars = IntStream.range(0, decisions.size())
                .mapToObj(index -> racingCar(names.get(index), decisions.get(index)))
                .toList();

        return new RacingCars(cars);
    }

    private static RacingCar racingCar(String name, List<Boolean> decisions) {
        return new RacingCar(Name.of(name), new TestAdvanceDecider(decisions));
    }
}
