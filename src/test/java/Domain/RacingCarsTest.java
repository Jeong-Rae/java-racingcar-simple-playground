package Domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.entry;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Arrays;
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
    class 참가_자동차를_생성할_때 {

        @Test
        void 참가_순서대로_시작_위치를_생성합니다() {
            var names = names();
            var cars = new RacingCars(names);

            var actualPositions = cars.positions();

            assertThat(actualPositions).containsExactly(
                    entry(names.get(0), Position.ZERO),
                    entry(names.get(1), Position.ZERO),
                    entry(names.get(2), Position.ZERO)
            );
        }

        @Test
        void 자동차_이름_목록이_null이면_예외를_발생시킵니다() {
            List<Name> names = null;
            ThrowingCallable executable = () -> new RacingCars(names);

            assertThatThrownBy(executable)
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("참가 자동차 이름 목록은 null일 수 없습니다.");
        }

        @Test
        void 자동차_이름_목록에_null이_포함되면_명시적_예외를_발생시킵니다() {
            var names = Arrays.asList(Name.of("RYAN"), null);
            ThrowingCallable executable = () -> new RacingCars(names);

            assertThatThrownBy(executable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("자동차 이름은 null일 수 없습니다.");
        }

        @Test
        void 자동차_목록이_비어_있으면_예외를_발생시킵니다() {
            var names = List.<Name>of();
            ThrowingCallable executable = () -> new RacingCars(names);

            assertThatThrownBy(executable)
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 자동차_이름이_중복되면_예외를_발생시킵니다() {
            var names = List.of(Name.of("RYAN"), Name.of("RYAN"));
            ThrowingCallable executable = () -> new RacingCars(names);

            assertThatThrownBy(executable)
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 현재_위치를_조회할_때 {

        @Test
        void 조회한_위치_정보는_다음_라운드의_변경에_영향받지_않습니다() {
            var ryan = Name.of("RYAN");
            var cars = new RacingCars(List.of(ryan));
            var initialPositions = cars.positions();

            cars.advance(new TestAdvanceDecider(true));
            var actualInitialPosition = initialPositions.get(ryan);

            assertThat(actualInitialPosition).isEqualTo(Position.ZERO);
        }

        @Test
        void 조회한_위치_정보를_수정하면_예외를_발생시킵니다() {
            var cars = new RacingCars(names());
            var positions = cars.positions();
            ThrowingCallable executable =
                    () -> positions.put(Name.of("RYAN"), Position.of(1));

            assertThatThrownBy(executable)
                    .isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @Nested
    class 자동차들을_전진할_때 {

        @Test
        void 하나의_판단_정책을_모든_자동차에_순서대로_적용합니다() {
            var names = names();
            var cars = new RacingCars(names);
            var decider = new TestAdvanceDecider(List.of(true, false, true));

            cars.advance(decider);
            var actualPositions = cars.positions();

            assertThat(actualPositions).containsExactly(
                    entry(names.get(0), Position.of(1)),
                    entry(names.get(1), Position.ZERO),
                    entry(names.get(2), Position.of(1))
            );
        }

        @Test
        void 전진_판단_정책이_null이면_예외를_발생시킵니다() {
            var cars = new RacingCars(names());
            AdvanceDecider decider = null;
            ThrowingCallable executable = () -> cars.advance(decider);

            assertThatThrownBy(executable)
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    class 선두_자동차를_구할_때 {

        @ParameterizedTest
        @MethodSource("Domain.RacingCarsTest#선두_자동차_케이스")
        void 자동차별_위치에_따라_가장_앞선_자동차들을_반환합니다(
                List<Boolean> decisions,
                int rounds,
                List<String> expectedNames
        ) {
            var cars = new RacingCars(names());
            var decider = new TestAdvanceDecider(decisions);

            IntStream.range(0, rounds).forEach(ignored -> cars.advance(decider));
            var actualNames = cars.leaders().stream().map(Name::value).toList();

            assertThat(actualNames).containsExactlyElementsOf(expectedNames);
        }
    }

    static Stream<Arguments> 선두_자동차_케이스() {
        return Stream.of(
                arguments(
                        List.of(true, true, false, true, false, false),
                        2,
                        List.of("RYAN")
                ),
                arguments(
                        List.of(true, true, false, false, false, false),
                        2,
                        List.of("RYAN", "MUZI")
                ),
                arguments(
                        List.of(false, false, false),
                        1,
                        List.of("RYAN", "MUZI", "춘식")
                )
        );
    }

    private static List<Name> names() {
        return List.of(Name.of("RYAN"), Name.of("MUZI"), Name.of("춘식"));
    }
}
