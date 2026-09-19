package Domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class NameTest {

    @Test
    void 한글과_영문으로_이름을_생성한다() {
        assertThat(new Name("pobi").value()).isEqualTo("pobi");
        assertThat(new Name("포비").value()).isEqualTo("포비");
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

    @Test
    void 한글과_영문_외의_문자는_허용하지_않는다() {
        assertThatThrownBy(() -> new Name("pobi1"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Name("포비!"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
