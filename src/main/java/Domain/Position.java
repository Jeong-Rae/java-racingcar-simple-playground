package Domain;

public record Position(int value) implements Comparable<Position> {

    public static final Position ZERO = Position.of(0);

    private static final int MIN_POSITION = 0;
    private static final int MAX_POSITION = 100;

    public Position {
        if (value < MIN_POSITION || value > MAX_POSITION) {
            throw new IllegalArgumentException("자동차 위치는 0부터 100 사이여야 합니다.");
        }
    }

    public static Position of(int value) {
        return new Position(value);
    }

    public static Position start() {
        return ZERO;
    }

    public Position advance() {
        return Position.of(value + 1);
    }

    @Override
    public int compareTo(Position other) {
        return Integer.compare(value, other.value);
    }
}
