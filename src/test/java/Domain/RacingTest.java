package Domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.List;
import java.util.stream.Stream;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RacingTest {

    @Nested
    class 경주를_준비할_때 {

        @Test
        void ready로_생성하면_READY_상태와_0개의_완료_라운드를_가집니다() {
            var racing = racing(3, false);
            var expectedStatus = RacingStatus.READY;
            var expectedCompletedRounds = 0;

            var actualStatus = racing.status();
            var actualCompletedRounds = racing.completedRounds();

            assertThat(actualStatus).isEqualTo(expectedStatus);
            assertThat(actualCompletedRounds).isEqualTo(expectedCompletedRounds);
        }
    }

    @Nested
    class 경주를_시작할_때 {

        @Test
        void READY_상태에서_start를_호출하면_RACING_상태가_됩니다() {
            var racing = racing(3, false);
            var expectedStatus = RacingStatus.RACING;

            racing.start();
            var actualStatus = racing.status();

            assertThat(actualStatus).isEqualTo(expectedStatus);
        }

        @Test
        void READY가_아닌_상태에서_start를_호출하면_예외를_발생시킵니다() {
            var racing = racing(3, false);
            racing.start();
            ThrowingCallable executable = racing::start;

            assertThatThrownBy(executable)
                    .isInstanceOf(IllegalStateException.class);
        }
    }

    @Nested
    class 한_라운드를_진행할_때 {

        @Test
        void RACING_상태에서_advance를_호출하면_한_라운드를_진행합니다() {
            var racing = racing(3, true);
            racing.start();
            var expectedPosition = Position.of(1);

            racing.advance();
            var actualPosition = racing.positions().get(Name.of("RYAN"));

            assertThat(actualPosition).isEqualTo(expectedPosition);
            assertThat(racing.completedRounds()).isEqualTo(1);
        }

        @Test
        void 마지막_라운드를_완료하면_FINISHED_상태가_됩니다() {
            var racing = racing(2, true);
            racing.start();

            racing.advance();
            racing.advance();
            var actualStatus = racing.status();

            assertThat(actualStatus).isEqualTo(RacingStatus.FINISHED);
        }

        @Test
        void RACING_상태가_아니면_advance를_호출할_때_예외를_발생시킵니다() {
            var racing = racing(1, false);
            ThrowingCallable executable = racing::advance;

            assertThatThrownBy(executable)
                    .isInstanceOf(IllegalStateException.class);
        }

        @Test
        void 종료된_경주에서_advance를_호출하면_예외를_발생시킵니다() {
            var racing = racing(1, false);
            racing.start();
            racing.advance();
            ThrowingCallable executable = racing::advance;

            assertThatThrownBy(executable)
                    .isInstanceOf(IllegalStateException.class);
        }
    }

    @Nested
    class 우승자를_조회할_때 {

        @ParameterizedTest
        @MethodSource("Domain.RacingTest#우승자_케이스")
        void 경주가_종료되면_가장_앞선_자동차들을_반환합니다(
                List<Boolean> decisions,
                List<String> expectedNames
        ) {
            var racing = racing(2, decisions);

            finish(racing);
            var actualNames = racing.winners().stream().map(Name::value).toList();

            assertThat(actualNames).containsExactlyElementsOf(expectedNames);
        }

        @Test
        void 경주가_종료되지_않으면_우승자_조회시_예외를_발생시킵니다() {
            var racing = racing(1, false);
            ThrowingCallable executable = racing::winners;

            assertThatThrownBy(executable)
                    .isInstanceOf(IllegalStateException.class);
        }
    }

    @Test
    void 100라운드에서_항상_전진하면_최종_위치가_100이_됩니다() {
        var racing = racing(100, true);
        var expectedPosition = Position.of(100);

        finish(racing);
        var actualPosition = racing.positions().get(Name.of("RYAN"));

        assertThat(actualPosition).isEqualTo(expectedPosition);
    }

    static Stream<Arguments> 우승자_케이스() {
        return Stream.of(
                arguments(
                        List.of(true, true, false, true, false, false),
                        List.of("RYAN")
                ),
                arguments(
                        List.of(true, true, false, false, false, false),
                        List.of("RYAN", "MUZI")
                )
        );
    }

    private static Racing racing(int rounds, boolean decision) {
        var cars = new RacingCars(List.of(Name.of("RYAN")));

        return Racing.ready(cars, Round.of(rounds), new TestAdvanceDecider(decision));
    }

    private static Racing racing(int rounds, List<Boolean> decisions) {
        var cars = new RacingCars(
                List.of(Name.of("RYAN"), Name.of("MUZI"), Name.of("춘식"))
        );

        return Racing.ready(cars, Round.of(rounds), new TestAdvanceDecider(decisions));
    }

    private static void finish(Racing racing) {
        racing.start();
        while (racing.status() == RacingStatus.RACING) {
            racing.advance();
        }
    }
}
