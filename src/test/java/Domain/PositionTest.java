package Domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class PositionTest {

    @Test
    void 시작_위치는_ZERO이다() {
        assertThat(Position.start()).isEqualTo(Position.ZERO);
    }

    @Test
    void 위치는_한_칸_전진할_수_있다() {
        var position = Position.start().advance();

        assertThat(position.value()).isEqualTo(1);
    }

    @Test
    void 위치는_0보다_작을_수_없다() {
        assertThatThrownBy(() -> new Position(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 위치는_100보다_클_수_없다() {
        assertThatThrownBy(() -> new Position(101))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
