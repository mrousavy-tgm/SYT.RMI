package utils;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JLogger implements Logger<String> {
    private PrintStream stream;

    public JLogger(PrintStream stream) {
        if (stream == null){
            this.stream = System.out;
        } else{
            this.stream = stream;
        }
    }

    @Override
    public void Log(Severity severity, String message) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss:ms");
        LocalDateTime now = LocalDateTime.now();

        String buffer = String.format("[%s] [%s]: %s", now.toString(), severity.toString(), message);
        stream.println(buffer);
    }
}
