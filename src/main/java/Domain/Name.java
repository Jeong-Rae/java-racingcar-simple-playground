package Domain;

import java.util.Objects;

public record Name(String value) {

    private static final int MAX_LENGTH = 5;

    public Name {
        Objects.requireNonNull(value, "자동차 이름은 null일 수 없습니다.");
        if (value.isBlank()) {
            throw new IllegalArgumentException("자동차 이름은 공백일 수 없습니다.");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("자동차 이름은 5자 이하여야 합니다.");
        }
    }
}
