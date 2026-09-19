package Domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RacingCarTest {

    @Nested
    class advance를_호출할_때 {

        @ParameterizedTest
        @MethodSource("Domain.RacingCarTest#전진_판단_케이스")
        void 판단_결과에_따라_최종_위치가_결정됩니다(
                List<Boolean> decisions,
                int expectedPosition
        ) {
            var car = new RacingCar(Name.of("라이언"), new TestAdvanceDecider(decisions));
            var expected = Position.of(expectedPosition);

            decisions.forEach(ignored -> car.advance());
            var actual = car.position();

            assertThat(actual).isEqualTo(expected);
        }
    }

    static Stream<Arguments> 전진_판단_케이스() {
        return Stream.of(
                arguments(List.of(true), 1),
                arguments(List.of(false), 0),
                arguments(List.of(true, false, true), 2),
                arguments(List.of(false, false, false), 0),
                arguments(List.of(true, true, true), 3)
        );
    }
}
