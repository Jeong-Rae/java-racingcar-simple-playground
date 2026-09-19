package Domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.random.RandomGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("RandomAdvanceDecider")
class RandomAdvanceDeciderTest {

    @Nested
    @DisplayName("랜덤 값으로 전진 여부를 판단할 때")
    class Decision {

        @ParameterizedTest(name = "랜덤 값이 {0}이면, shouldAdvance가 {1}를 반환합니다")
        @CsvSource({
                "3, false",
                "4, true",
                "5, true"
        })
        void decidesAtBoundary(int randomValue, boolean expected) {
            var decider = new RandomAdvanceDecider(new FixedRandomGenerator(randomValue));

            assertThat(decider.shouldAdvance()).isEqualTo(expected);
        }
    }

    private record FixedRandomGenerator(int value) implements RandomGenerator {

        @Override
        public int nextInt(int bound) {
            return value;
        }

        @Override
        public long nextLong() {
            return value;
        }
    }
}
