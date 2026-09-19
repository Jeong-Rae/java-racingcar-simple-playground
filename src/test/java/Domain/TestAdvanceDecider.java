package Domain;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

final class TestAdvanceDecider implements AdvanceDecider {

    private final Queue<Boolean> decisions;
    private final Boolean repeatedDecision;

    TestAdvanceDecider(boolean decision) {
        this.decisions = new ArrayDeque<>();
        this.repeatedDecision = decision;
    }

    TestAdvanceDecider(List<Boolean> decisions) {
        this.decisions = new ArrayDeque<>(decisions);
        this.repeatedDecision = null;
    }

    @Override
    public boolean shouldAdvance() {
        if (repeatedDecision != null) {
            return repeatedDecision;
        }
        if (decisions.isEmpty()) {
            throw new IllegalStateException("더 이상 전진 판단 결과가 없습니다.");
        }
        return decisions.remove();
    }
}
