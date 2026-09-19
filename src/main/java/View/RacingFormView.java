package View;

import Common.ConsoleReader;
import Common.ConsoleWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class RacingFormView {

    private static final String NAME_DELIMITER = ",";
    private static final String CAR_NAMES_PROMPT =
            "경주할 자동차 이름을 쉼표(,)로 구분해 입력해 주세요.";
    private static final String RACE_COUNT_PROMPT = "시도할 횟수를 입력해 주세요.";

    private final ConsoleReader reader;
    private final ConsoleWriter writer;

    public RacingFormView(ConsoleReader reader, ConsoleWriter writer) {
        this.reader = Objects.requireNonNull(
                reader,
                "콘솔 입력기(ConsoleReader)는 null일 수 없습니다."
        );
        this.writer = Objects.requireNonNull(
                writer,
                "콘솔 출력기(ConsoleWriter)는 null일 수 없습니다."
        );
    }

    public List<String> readCarNames() {
        writer.writeLine(CAR_NAMES_PROMPT);

        return Arrays.stream(reader.readLine().split(NAME_DELIMITER))
                .map(String::trim)
                .toList();
    }

    public int readRaceCount() {
        writer.writeLine(RACE_COUNT_PROMPT);

        return Integer.parseInt(reader.readLine().trim());
    }
}
