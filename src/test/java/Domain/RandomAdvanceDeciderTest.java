package Domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.random.RandomGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class RandomAdvanceDeciderTest {

    @Nested
    class 랜덤_값으로_전진_여부를_판단할_때 {

        @ParameterizedTest
        @CsvSource({
                "3, false",
                "4, true",
                "5, true"
        })
        void 랜덤_값이_기준값의_경계이면_shouldAdvance가_예상_결과를_반환합니다(
                int randomValue,
                boolean expected
        ) {
            var decider = new RandomAdvanceDecider(new FixedRandomGenerator(randomValue));

            var actual = decider.shouldAdvance();

            assertThat(actual).isEqualTo(expected);
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
