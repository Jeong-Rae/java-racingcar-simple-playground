package Domain;

import java.util.Objects;
import java.util.regex.Pattern;

public record Name(String value) {

    private static final int MAX_LENGTH = 5;
    private static final Pattern ALLOWED_NAME = Pattern.compile("^[가-힣a-zA-Z]+$");

    public Name {
        Objects.requireNonNull(value, "자동차 이름은 null일 수 없습니다.");
        if (value.isBlank()) {
            throw new IllegalArgumentException("자동차 이름은 공백일 수 없습니다.");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("자동차 이름은 5자 이하여야 합니다.");
        }
        if (!ALLOWED_NAME.matcher(value).matches()) {
            throw new IllegalArgumentException("자동차 이름은 한글과 영문만 사용할 수 있습니다.");
        }
    }

    public static Name of(String value) {
        return new Name(value);
    }
}
