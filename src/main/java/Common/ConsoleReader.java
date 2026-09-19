package Common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

public final class ConsoleReader {

    private final BufferedReader reader;

    private ConsoleReader(Reader reader) {
        this.reader = new BufferedReader(reader);
    }

    public static ConsoleReader system() {
        return new ConsoleReader(
                new InputStreamReader(System.in, StandardCharsets.UTF_8)
        );
    }

    public static ConsoleReader string(String input) {
        return new ConsoleReader(new StringReader(input));
    }

    public String readLine() {
        try {
            var line = reader.readLine();
            if (line == null) {
                throw new IllegalStateException("콘솔 입력이 존재하지 않습니다.");
            }
            return line;
        } catch (IOException exception) {
            throw new UncheckedIOException("콘솔 입력을 읽을 수 없습니다.", exception);
        }
    }
}
