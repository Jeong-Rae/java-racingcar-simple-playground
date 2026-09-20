package View;

import Common.ConsoleReader;
import Common.ConsoleWriter;
import java.util.Arrays;
import java.util.List;

public final class RacingFormView {

    private static final String NAME_DELIMITER = ",";

    private final ConsoleReader reader;
    private final ConsoleWriter writer;

    public RacingFormView(ConsoleReader reader, ConsoleWriter writer) {
        requireDependencies(reader, writer);
        this.reader = reader;
        this.writer = writer;
    }

    public List<String> readCarNames() {
        var prompt = "경주할 자동차 이름을 쉼표(,)로 구분해 입력해 주세요.";
        writer.writeLine(prompt);

        return Arrays.stream(reader.readLine().split(NAME_DELIMITER))
                .map(String::trim)
                .toList();
    }

    public int readRaceCount() {
        var prompt = "시도할 횟수를 입력해 주세요.";
        writer.writeLine(prompt);

        return Integer.parseInt(reader.readLine().trim());
    }

    private static void requireDependencies(ConsoleReader reader, ConsoleWriter writer) {
        if (reader == null) {
            throw new IllegalArgumentException("콘솔 입력기(ConsoleReader)는 null일 수 없습니다.");
        }
        if (writer == null) {
            throw new IllegalArgumentException("콘솔 출력기(ConsoleWriter)는 null일 수 없습니다.");
        }
    }
}
