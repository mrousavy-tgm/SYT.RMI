import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;


public interface Logger<T> {
    void Log(Severity severity, T message);

    public enum Severity {
        Debug,
        Info,
        Warning,
        Error
    }
}


