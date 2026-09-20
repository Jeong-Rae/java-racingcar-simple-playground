package Domain;

import java.util.regex.Pattern;

public record Name(String value) {

    private static final int MAX_LENGTH = 5;
    private static final Pattern ALLOWED_NAME = Pattern.compile("^[가-힣a-zA-Z]+$");

    public Name {
        if (value == null) {
            throw new IllegalArgumentException("자동차 이름은 null일 수 없습니다.");
        }

        var blank = value.isBlank();
        if (blank) {
            throw new IllegalArgumentException("자동차 이름은 공백일 수 없습니다.");
        }

        var exceedsMaxLength = value.length() > MAX_LENGTH;
        if (exceedsMaxLength) {
            throw new IllegalArgumentException("자동차 이름은 5자 이하여야 합니다.");
        }

        var containsUnsupportedCharacter = !ALLOWED_NAME.matcher(value).matches();
        if (containsUnsupportedCharacter) {
            throw new IllegalArgumentException("자동차 이름은 한글과 영문만 사용할 수 있습니다.");
        }
    }

    public static Name of(String value) {
        return new Name(value);
    }
}
