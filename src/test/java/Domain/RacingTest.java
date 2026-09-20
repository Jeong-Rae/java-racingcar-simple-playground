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
import org.junit.jupiter.params.provider.ValueSource;

class RacingTest {

    @Nested
    class 경주를_준비할_때 {

        @Test
        void ready로_생성하면_완료한_라운드가_0입니다() {
            var racing = racing(3, false);
            var expectedCompletedRounds = 0;

            var actualCompletedRounds = racing.completedRounds();

            assertThat(actualCompletedRounds).isEqualTo(expectedCompletedRounds);
        }

        @Test
        void 경주_참가_자동차_이름_목록이_null이면_예외를_발생시킵니다() {
            List<Name> names = null;
            var round = Round.of(1);
            var decider = new TestAdvanceDecider(false);
            ThrowingCallable executable = () -> Racing.ready(names, round, decider);

            assertThatThrownBy(executable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("경주 참가 자동차 이름 목록은 null일 수 없습니다.");
        }

        @Test
        void 전체_라운드가_null이면_예외를_발생시킵니다() {
            var names = List.of(Name.of("RYAN"));
            Round round = null;
            var decider = new TestAdvanceDecider(false);
            ThrowingCallable executable = () -> Racing.ready(names, round, decider);

            assertThatThrownBy(executable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("전체 경주 횟수는 null일 수 없습니다.");
        }

        @Test
        void 전진_판단_정책이_null이면_예외를_발생시킵니다() {
            var names = List.of(Name.of("RYAN"));
            var round = Round.of(1);
            AdvanceDecider decider = null;
            ThrowingCallable executable = () -> Racing.ready(names, round, decider);

            assertThatThrownBy(executable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("전진 여부 판단 정책은 null일 수 없습니다.");
        }
    }

    @Nested
    class 경주를_시작할_때 {

        @Test
        void start를_호출하면_라운드를_진행할_수_있습니다() {
            var racing = racing(1, false);
            var expectedAdvanced = true;

            racing.start();
            var actualAdvanced = racing.advanceIfPossible();

            assertThat(actualAdvanced).isEqualTo(expectedAdvanced);
        }

        @Test
        void 이미_시작한_경주에서_start를_호출하면_예외를_발생시킵니다() {
            var racing = racing(2, false);
            racing.start();
            ThrowingCallable executable = racing::start;

            assertThatThrownBy(executable)
                    .isInstanceOf(IllegalStateException.class);
        }

        @Test
        void 종료한_경주에서_start를_호출하면_예외를_발생시킵니다() {
            var racing = racing(1, false);
            racing.start();
            racing.advanceIfPossible();
            ThrowingCallable executable = racing::start;

            assertThatThrownBy(executable)
                    .isInstanceOf(IllegalStateException.class);
        }
    }

    @Nested
    class 한_라운드를_진행할_때 {

        @Test
        void 시작하기_전에는_라운드를_진행하지_않습니다() {
            var racing = racing(1, true);
            var expectedAdvanced = false;

            var actualAdvanced = racing.advanceIfPossible();

            assertThat(actualAdvanced).isEqualTo(expectedAdvanced);
        }

        @Test
        void 진행할_수_있으면_true를_반환하고_위치를_변경합니다() {
            var racing = racing(3, true);
            racing.start();
            var expectedPosition = Position.of(1);

            var actualAdvanced = racing.advanceIfPossible();
            var actualPosition = racing.positions().get(Name.of("RYAN"));

            assertThat(actualAdvanced).isTrue();
            assertThat(actualPosition).isEqualTo(expectedPosition);
        }

        @Test
        void 진행할_수_있으면_완료한_라운드가_1_증가합니다() {
            var racing = racing(3, false);
            racing.start();
            var expectedCompletedRounds = 1;

            racing.advanceIfPossible();
            var actualCompletedRounds = racing.completedRounds();

            assertThat(actualCompletedRounds).isEqualTo(expectedCompletedRounds);
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 2, 100})
        void 설정한_라운드_수만큼만_진행합니다(int rounds) {
            var racing = racing(rounds, false);
            racing.start();
            var expectedAdvancedRounds = rounds;

            var actualAdvancedRounds = 0;
            while (racing.advanceIfPossible()) {
                actualAdvancedRounds++;
            }

            assertThat(actualAdvancedRounds).isEqualTo(expectedAdvancedRounds);
        }

        @Test
        void 모든_라운드를_완료하면_더_이상_진행하지_않습니다() {
            var racing = racing(1, false);
            racing.start();
            racing.advanceIfPossible();
            var expectedAdvanced = false;

            var actualAdvanced = racing.advanceIfPossible();

            assertThat(actualAdvanced).isEqualTo(expectedAdvanced);
        }
    }

    @Nested
    class 현재_위치를_조회할_때 {

        @Test
        void 시작하기_전에도_현재_위치를_조회할_수_있습니다() {
            var racing = racing(1, false);
            var expectedPosition = Position.ZERO;

            var actualPosition = racing.positions().get(Name.of("RYAN"));

            assertThat(actualPosition).isEqualTo(expectedPosition);
        }

        @Test
        void 라운드를_진행한_뒤_현재_위치를_조회할_수_있습니다() {
            var racing = racing(2, true);
            racing.start();
            racing.advanceIfPossible();
            var expectedPosition = Position.of(1);

            var actualPosition = racing.positions().get(Name.of("RYAN"));

            assertThat(actualPosition).isEqualTo(expectedPosition);
        }
    }

    @Nested
    class 우승자를_조회할_때 {

        @ParameterizedTest
        @MethodSource("Domain.RacingTest#우승자_케이스")
        void 모든_라운드를_완료하면_가장_앞선_자동차들을_반환합니다(
                List<Boolean> decisions,
                List<String> expectedNames
        ) {
            var racing = racing(2, decisions);
            racing.start();

            while (racing.advanceIfPossible()) {
            }
            var actualNames = racing.winners().stream().map(Name::value).toList();

            assertThat(actualNames).containsExactlyElementsOf(expectedNames);
        }

        @Test
        void 시작하기_전에_우승자를_조회하면_예외를_발생시킵니다() {
            var racing = racing(1, false);
            ThrowingCallable executable = racing::winners;

            assertThatThrownBy(executable)
                    .isInstanceOf(IllegalStateException.class);
        }

        @Test
        void 진행_중에_우승자를_조회하면_예외를_발생시킵니다() {
            var racing = racing(2, false);
            racing.start();
            ThrowingCallable executable = racing::winners;

            assertThatThrownBy(executable)
                    .isInstanceOf(IllegalStateException.class);
        }
    }

    @Test
    void 100라운드에서_항상_전진하면_최종_위치가_100이_됩니다() {
        var racing = racing(100, true);
        var expectedPosition = Position.of(100);
        racing.start();

        while (racing.advanceIfPossible()) {
        }
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
        var names = List.of(Name.of("RYAN"));

        return Racing.ready(names, Round.of(rounds), new TestAdvanceDecider(decision));
    }

    private static Racing racing(int rounds, List<Boolean> decisions) {
        var names = List.of(Name.of("RYAN"), Name.of("MUZI"), Name.of("춘식"));

        return Racing.ready(names, Round.of(rounds), new TestAdvanceDecider(decisions));
    }
}
