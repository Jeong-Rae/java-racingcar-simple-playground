package View;

import Common.ConsoleWriter;
import java.util.Map;
import java.util.Objects;

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
        validateRound(currentRound, totalRound);
        validatePositions(positions);

        writer.writeLine("라운드 %d/%d", currentRound, totalRound);
        positions.forEach((name, position) ->
                writer.writeLine("%s: %s", name, POSITION_MARK.repeat(position))
        );
    }

    private static void validateRound(int currentRound, int totalRound) {
        var isInvalidTotalRound = totalRound < 1;
        if (isInvalidTotalRound) {
            throw new IllegalArgumentException("전체 라운드는 1 이상이어야 합니다.");
        }

        var isInvalidCurrentRound = currentRound < 1 || currentRound > totalRound;
        if (isInvalidCurrentRound) {
            throw new IllegalArgumentException("현재 라운드는 전체 라운드 범위 안에 있어야 합니다.");
        }
    }

    private static void validatePositions(Map<String, Integer> positions) {
        if (positions == null) {
            throw new IllegalArgumentException("자동차 위치 정보는 null일 수 없습니다.");
        }

        var isEmpty = positions.isEmpty();
        if (isEmpty) {
            throw new IllegalArgumentException("자동차 위치 정보는 비어 있을 수 없습니다.");
        }

        var hasNullName = positions.keySet().stream().anyMatch(Objects::isNull);
        if (hasNullName) {
            throw new IllegalArgumentException("자동차 이름은 null일 수 없습니다.");
        }

        var hasNullPosition = positions.values().stream().anyMatch(Objects::isNull);
        if (hasNullPosition) {
            throw new IllegalArgumentException("자동차 위치는 null일 수 없습니다.");
        }

        var hasNegativePosition = positions.values().stream()
                .anyMatch(position -> position < 0);
        if (hasNegativePosition) {
            throw new IllegalArgumentException("자동차 위치는 0 이상이어야 합니다.");
        }
    }
}
