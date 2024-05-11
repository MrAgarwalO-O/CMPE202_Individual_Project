import java.util.*;

public class RequestLogHandler extends LogHandler {
    private Map<String, RequestData> requestLogs;

    public RequestLogHandler(Map<String, RequestData> requestLogs) {
        this.requestLogs = requestLogs;
    }

    @Override
    public boolean handleLog(String line, Map<String, String> parsedLine) {
        if (parsedLine.containsKey("request_url")) {
            String url = parsedLine.get("request_url");
            int responseTime = Integer.parseInt(parsedLine.get("response_time_ms"));
            String status = parsedLine.get("response_status");
            
            // Get or create the RequestData object for this URL
            RequestData requestData = requestLogs.computeIfAbsent(url, k -> new RequestData());
            
            // Add response time
            requestData.addResponseTime(responseTime);
            
            // Increment the status code category
            requestData.incrementStatusCodeCategory(status);

            return true;
        }
        return nextHandler != null && nextHandler.handleLog(line, parsedLine);
    }
}
