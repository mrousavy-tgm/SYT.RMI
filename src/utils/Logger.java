package utils;

public interface Logger<T> {
    void Log(Severity severity, T message);

    public enum Severity {
        Debug,
        Info,
        Warning,
        Error
    }
}


