package Common;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

public final class ConsoleWriter {

    private final PrintWriter writer;

    private ConsoleWriter(Writer writer) {
        this.writer = new PrintWriter(writer);
    }

    public static ConsoleWriter system() {
        return new ConsoleWriter(
                new OutputStreamWriter(System.out, StandardCharsets.UTF_8)
        );
    }

    public static ConsoleWriter string(StringWriter writer) {
        if (writer == null) {
            throw new IllegalArgumentException("문자열 출력기(StringWriter)는 null일 수 없습니다.");
        }
        return new ConsoleWriter(writer);
    }

    public void write(String template, Object... values) {
        writer.print(template.formatted(values));
        writer.flush();
    }

    public void writeLine(String template, Object... values) {
        writer.println(template.formatted(values));
        writer.flush();
    }
}
