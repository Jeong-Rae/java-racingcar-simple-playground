package Domain;

public record Round(int value) {

    private static final int MIN_ROUND = 1;

    public Round {
        if (value < MIN_ROUND) {
            throw new IllegalArgumentException("라운드는 1 이상이어야 합니다.");
        }
    }
}
