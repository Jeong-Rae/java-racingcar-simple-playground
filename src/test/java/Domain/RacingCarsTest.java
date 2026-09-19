package Domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RacingCarsTest {

    @Nested
    class 자동차들을_전진할_때 {

        @Test
        void advance를_호출하면_각_자동차가_자신의_판단_결과에_따라_전진합니다() {
            var ryan = new RacingCar(Name.of("RYAN"), new TestAdvanceDecider(true));
            var muzi = new RacingCar(Name.of("MUZI"), new TestAdvanceDecider(false));
            var cars = new RacingCars(List.of(ryan, muzi));
            var expectedRyanPosition = Position.of(1);
            var expectedMuziPosition = Position.ZERO;

            cars.advance();
            var actualRyanPosition = ryan.position();
            var actualMuziPosition = muzi.position();

            assertThat(actualRyanPosition).isEqualTo(expectedRyanPosition);
            assertThat(actualMuziPosition).isEqualTo(expectedMuziPosition);
        }
    }

    @Nested
    class 선두_자동차를_구할_때 {

        @ParameterizedTest
        @MethodSource("Domain.RacingCarsTest#선두_자동차_케이스")
        void 자동차별_위치가_주어지면_가장_앞선_자동차들을_반환합니다(
                List<List<Boolean>> decisions,
                List<String> expectedNames
        ) {
            var cars = cars(decisions);

            decisions.getFirst().forEach(ignored -> cars.advance());
            var actualLeaders = cars.leaders();

            assertThat(actualLeaders)
                    .extracting(car -> car.name().value())
                    .containsExactlyInAnyOrderElementsOf(expectedNames);
        }
    }

    @Test
    void 자동차_목록이_비어_있으면_예외를_발생시킵니다() {
        var emptyCars = List.<RacingCar>of();
        ThrowingCallable executable = () -> new RacingCars(emptyCars);

        assertThatThrownBy(executable)
                .isInstanceOf(IllegalArgumentException.class);
    }

    static Stream<Arguments> 선두_자동차_케이스() {
        return Stream.of(
                arguments(
                        List.of(
                                List.of(true, true),
                                List.of(true, false),
                                List.of(false, false)
                        ),
                        List.of("RYAN")
                ),
                arguments(
                        List.of(
                                List.of(true, false),
                                List.of(true, false),
                                List.of(false, false)
                        ),
                        List.of("RYAN", "MUZI")
                )
        );
    }

    private static RacingCars cars(List<List<Boolean>> decisions) {
        var names = List.of("RYAN", "MUZI", "춘식");
        var cars = IntStream.range(0, decisions.size())
                .mapToObj(index -> new RacingCar(
                        Name.of(names.get(index)),
                        new TestAdvanceDecider(decisions.get(index))
                ))
                .toList();

        return new RacingCars(cars);
    }
}
