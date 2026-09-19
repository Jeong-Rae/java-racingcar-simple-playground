package Domain;

public record Round(int value) {

    private static final int MIN_ROUND = 1;
    private static final int MAX_ROUND = 100;

    public Round {
        if (value < MIN_ROUND || value > MAX_ROUND) {
            throw new IllegalArgumentException("라운드는 1부터 100 사이여야 합니다.");
        }
    }

    public static Round of(int value) {
        return new Round(value);
    }
}
