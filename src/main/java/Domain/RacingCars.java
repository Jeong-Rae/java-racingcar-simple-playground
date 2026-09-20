package Domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class RacingCars {

    private final Map<Name, Position> positions;

    public RacingCars(List<Name> names) {
        validate(names);

        var participants = List.copyOf(names);
        this.positions = initialPositionsFrom(participants);
    }

    public void advance(AdvanceDecider advanceDecider) {
        if (advanceDecider == null) {
            throw new IllegalArgumentException("전진 여부 판단 정책은 null일 수 없습니다.");
        }
        positions.replaceAll((name, position) -> advance(position, advanceDecider));
    }

    public Map<Name, Position> positions() {
        return Collections.unmodifiableMap(new LinkedHashMap<>(positions));
    }

    public List<Name> leaders() {
        var maxPosition = maxPosition();

        return positions.entrySet().stream()
                .filter(entry -> entry.getValue().equals(maxPosition))
                .map(Map.Entry::getKey)
                .toList();
    }

    private static void validate(List<Name> names) {
        if (names == null) {
            throw new IllegalArgumentException("참가 자동차 이름 목록은 null일 수 없습니다.");
        }
        if (names.isEmpty()) {
            throw new IllegalArgumentException("자동차는 한 대 이상이어야 합니다.");
        }
        if (names.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("자동차 이름은 null일 수 없습니다.");
        }
        if (hasDuplicatedNames(names)) {
            throw new IllegalArgumentException("같은 경주에 중복된 자동차 이름을 사용할 수 없습니다.");
        }
    }

    private static boolean hasDuplicatedNames(List<Name> names) {
        return new HashSet<>(names).size() != names.size();
    }

    private static Map<Name, Position> initialPositionsFrom(List<Name> names) {
        var positions = new LinkedHashMap<Name, Position>();
        names.forEach(name -> positions.put(name, Position.start()));

        return positions;
    }

    private static Position advance(Position position, AdvanceDecider advanceDecider) {
        if (!advanceDecider.shouldAdvance()) {
            return position;
        }
        return position.advance();
    }

    private Position maxPosition() {
        return Collections.max(positions.values());
    }
}
