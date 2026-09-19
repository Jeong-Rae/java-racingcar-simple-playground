package Domain;

import java.util.random.RandomGenerator;

public final class RandomAdvanceDecider implements AdvanceDecider {

    private static final int RANDOM_BOUND = 10;
    private static final int ADVANCE_THRESHOLD = 4;

    private final RandomGenerator randomGenerator;

    public RandomAdvanceDecider() {
        this(RandomGenerator.getDefault());
    }

    public RandomAdvanceDecider(RandomGenerator randomGenerator) {
        this.randomGenerator = randomGenerator;
    }

    @Override
    public boolean shouldAdvance() {
        return randomGenerator.nextInt(RANDOM_BOUND) >= ADVANCE_THRESHOLD;
    }
}
