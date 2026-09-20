package View;

import Common.ConsoleWriter;
import java.util.List;
import java.util.Map;

public final class RacingResultView {

    private static final String POSITION_MARK = "■";

    private final ConsoleWriter writer;

    public RacingResultView(ConsoleWriter writer) {
        if (writer == null) {
            throw new IllegalArgumentException("콘솔 출력기(ConsoleWriter)는 null일 수 없습니다.");
        }
        this.writer = writer;
    }

    public void printRound(
            int currentRound,
            int totalRound,
            Map<String, Integer> positions
    ) {
        writer.writeLine("라운드 %d/%d", currentRound, totalRound);
        positions.forEach((name, position) ->
                writer.writeLine("%s: %s", name, POSITION_MARK.repeat(position))
        );
    }

    public void printWinners(List<String> winners) {
        writer.writeLine("");
        writer.writeLine("최종 우승자: %s", String.join(", ", winners));
    }
}
