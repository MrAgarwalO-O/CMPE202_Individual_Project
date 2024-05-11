import java.util.*;

public class ApplicationLogHandler extends LogHandler {
    private Map<String, Integer> appLogs;

    public ApplicationLogHandler(Map<String, Integer> appLogs) {
        this.appLogs = appLogs;
    }

    @Override
    public boolean handleLog(String line, Map<String, String> parsedLine) {
        if (parsedLine.containsKey("level")) {
            appLogs.put(parsedLine.get("level"), appLogs.getOrDefault(parsedLine.get("level"), 0) + 1);
            return true;
        }
        return nextHandler != null && nextHandler.handleLog(line, parsedLine);
    }
}
