package Domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class NameTest {

    @Test
    void 이름을_생성한다() {
        var name = new Name("pobi");

        assertThat(name.value()).isEqualTo("pobi");
    }

    @Test
    void 공백_이름은_허용하지_않는다() {
        assertThatThrownBy(() -> new Name(" "))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 이름은_5자를_초과할_수_없다() {
        assertThatThrownBy(() -> new Name("racing"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
