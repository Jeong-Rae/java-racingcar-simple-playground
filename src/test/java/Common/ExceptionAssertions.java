package Common;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.function.Consumer;

public final class ExceptionAssertions {

    private ExceptionAssertions() {
    }

    public static <T> void assertThrowsForEach(
            List<T> values,
            Class<? extends Throwable> exceptionType,
            Consumer<T> executable
    ) {
        values.forEach(value -> assertThatThrownBy(() -> executable.accept(value))
                .isInstanceOf(exceptionType));
    }
}
