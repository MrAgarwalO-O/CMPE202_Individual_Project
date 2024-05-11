import java.util.*;

public abstract class LogHandler {
    protected LogHandler nextHandler;

    public void setNextHandler(LogHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public abstract boolean handleLog(String line, Map<String, String> parsedLine);
}
