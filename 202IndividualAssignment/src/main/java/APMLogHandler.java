import java.util.*;

public class APMLogHandler extends LogHandler {
    private Map<String, List<Integer>> apmMetrics;

    public APMLogHandler(Map<String, List<Integer>> apmMetrics) {
        this.apmMetrics = apmMetrics;
    }

    @Override
    public boolean handleLog(String line, Map<String, String> parsedLine) {
        if (parsedLine.containsKey("metric") && parsedLine.containsKey("value")) {
            apmMetrics.computeIfAbsent(parsedLine.get("metric"), k -> new ArrayList<>())
                       .add(Integer.parseInt(parsedLine.get("value")));
            return true;
        }
        return nextHandler != null && nextHandler.handleLog(line, parsedLine);
    }
}
