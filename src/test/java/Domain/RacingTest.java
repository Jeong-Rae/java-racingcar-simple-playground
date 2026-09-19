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
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class RacingTest {

    @Nested
    class 경주를_준비할_때 {

        @Test
        void ready로_생성하면_READY_상태가_됩니다() {
            var racing = racing(3, false);
            var expectedStatus = RacingStatus.READY;

            var actualStatus = racing.status();

            assertThat(actualStatus).isEqualTo(expectedStatus);
        }

        @Test
        void ready로_생성하면_완료한_라운드가_0입니다() {
            var racing = racing(3, false);
            var expectedCompletedRounds = 0;

            var actualCompletedRounds = racing.completedRounds();

            assertThat(actualCompletedRounds).isEqualTo(expectedCompletedRounds);
        }

        @Test
        void 경주_참가_자동차가_null이면_예외를_발생시킵니다() {
            RacingCars cars = null;
            var round = Round.of(1);
            var decider = new TestAdvanceDecider(false);
            ThrowingCallable executable = () -> Racing.ready(cars, round, decider);

            assertThatThrownBy(executable)
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        void 전체_라운드가_null이면_예외를_발생시킵니다() {
            var cars = new RacingCars(List.of(Name.of("RYAN")));
            Round round = null;
            var decider = new TestAdvanceDecider(false);
            ThrowingCallable executable = () -> Racing.ready(cars, round, decider);

            assertThatThrownBy(executable)
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        void 전진_판단_정책이_null이면_예외를_발생시킵니다() {
            var cars = new RacingCars(List.of(Name.of("RYAN")));
            var round = Round.of(1);
            AdvanceDecider decider = null;
            ThrowingCallable executable = () -> Racing.ready(cars, round, decider);

            assertThatThrownBy(executable)
                    .isInstanceOf(NullPointerException.class);
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

        @ParameterizedTest
        @EnumSource(value = RacingStatus.class, names = {"RACING", "FINISHED"})
        void READY가_아닌_상태에서_start를_호출하면_예외를_발생시킵니다(
                RacingStatus status
        ) {
            var racing = racingIn(status);
            ThrowingCallable executable = racing::start;

            assertThatThrownBy(executable)
                    .isInstanceOf(IllegalStateException.class);
        }
    }

    @Nested
    class 한_라운드를_진행할_때 {

        @Test
        void RACING_상태에서_advance를_호출하면_위치를_한_라운드만큼_변경합니다() {
            var racing = racing(3, true);
            racing.start();
            var expectedPosition = Position.of(1);

            racing.advance();
            var actualPosition = racing.positions().get(Name.of("RYAN"));

            assertThat(actualPosition).isEqualTo(expectedPosition);
        }

        @Test
        void RACING_상태에서_advance를_호출하면_완료한_라운드가_1_증가합니다() {
            var racing = racing(3, false);
            racing.start();
            var expectedCompletedRounds = 1;

            racing.advance();
            var actualCompletedRounds = racing.completedRounds();

            assertThat(actualCompletedRounds).isEqualTo(expectedCompletedRounds);
        }

        @ParameterizedTest
        @EnumSource(value = RacingStatus.class, names = {"READY", "FINISHED"})
        void RACING이_아닌_상태에서_advance를_호출하면_예외를_발생시킵니다(
                RacingStatus status
        ) {
            var racing = racingIn(status);
            ThrowingCallable executable = racing::advance;

            assertThatThrownBy(executable)
                    .isInstanceOf(IllegalStateException.class);
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 2, 100})
        void 마지막_라운드를_완료하면_FINISHED_상태가_됩니다(int rounds) {
            var racing = racing(rounds, false);
            racing.start();

            while (racing.status() == RacingStatus.RACING) {
                racing.advance();
            }
            var actualStatus = racing.status();

            assertThat(actualStatus).isEqualTo(RacingStatus.FINISHED);
        }
    }

    @Nested
    class 현재_위치를_조회할_때 {

        @ParameterizedTest
        @EnumSource(RacingStatus.class)
        void 모든_경주_상태에서_현재_위치를_조회할_수_있습니다(RacingStatus status) {
            var racing = racingIn(status);
            var expectedPosition = Position.ZERO;

            var actualPosition = racing.positions().get(Name.of("RYAN"));

            assertThat(actualPosition).isEqualTo(expectedPosition);
        }
    }

    @Nested
    class 우승자를_조회할_때 {

        @ParameterizedTest
        @MethodSource("Domain.RacingTest#우승자_케이스")
        void FINISHED_상태이면_가장_앞선_자동차들을_반환합니다(
                List<Boolean> decisions,
                List<String> expectedNames
        ) {
            var racing = racing(2, decisions);

            finish(racing);
            var actualNames = racing.winners().stream().map(Name::value).toList();

            assertThat(actualNames).containsExactlyElementsOf(expectedNames);
        }

        @ParameterizedTest
        @EnumSource(value = RacingStatus.class, names = {"READY", "RACING"})
        void FINISHED가_아닌_상태에서_우승자를_조회하면_예외를_발생시킵니다(
                RacingStatus status
        ) {
            var racing = racingIn(status);
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
                ),
                arguments(
                        List.of(false, false, false, false, false, false),
                        List.of("RYAN", "MUZI", "춘식")
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

    private static Racing racingIn(RacingStatus status) {
        var racing = racing(1, false);
        if (status == RacingStatus.READY) {
            return racing;
        }

        racing.start();
        if (status == RacingStatus.RACING) {
            return racing;
        }

        racing.advance();
        return racing;
    }

    private static void finish(Racing racing) {
        racing.start();
        while (racing.status() == RacingStatus.RACING) {
            racing.advance();
        }
    }
}
