import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

public class RequestLogHandlerTest {
    private Map<String, RequestData> requestLogs;
    private RequestLogHandler handler;

    @BeforeEach
    public void setUp() {
        requestLogs = new HashMap<>();
        handler = new RequestLogHandler(requestLogs);
    }

    @Test
    public void handleLog_ValidLogEntry_UpdatesRequestData() {
        // Arrange
        String line = "log entry";
        Map<String, String> parsedLine = Map.of(
            "request_url", "http://example.com",
            "response_time_ms", "200",
            "response_status", "200"
        );
        RequestData mockRequestData = mock(RequestData.class);
        requestLogs.put("http://example.com", mockRequestData);

        // Act
        boolean result = handler.handleLog(line, parsedLine);

        // Assert
        assertTrue(result, "Handler should have processed the log entry.");
        verify(mockRequestData).addResponseTime(200);
        verify(mockRequestData).incrementStatusCodeCategory("200");
    }

    @Test
    public void handleLog_NoRelevantKeys_ReturnsFalse() {
        // Arrange
        String line = "irrelevant log entry";
        Map<String, String> parsedLine = new HashMap<>();

        // Act
        boolean result = handler.handleLog(line, parsedLine);

        // Assert
        assertFalse(result, "Handler should not process the log entry.");
    }
}
