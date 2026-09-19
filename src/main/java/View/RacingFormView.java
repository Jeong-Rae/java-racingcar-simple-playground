package View;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class RacingFormView {

    private static final String NAME_DELIMITER = ",";

    private final ConsoleReader reader;

    public RacingFormView(ConsoleReader reader) {
        this.reader = Objects.requireNonNull(reader, "콘솔 입력기는 null일 수 없습니다.");
    }

    public List<String> readCarNames() {
        return Arrays.stream(reader.readLine().split(NAME_DELIMITER))
                .map(String::trim)
                .toList();
    }

    public int readRaceCount() {
        return Integer.parseInt(reader.readLine().trim());
    }
}
