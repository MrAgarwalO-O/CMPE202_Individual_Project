import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class APMLogHandlerTest {
    private APMLogHandler handler;
    private Map<String, List<Integer>> apmMetrics;

    @BeforeEach
    public void setUp() {
        apmMetrics = new HashMap<>();
        handler = new APMLogHandler(apmMetrics);
    }

    @Test
    public void handleLog_WhenMetricAndValuePresent_AddsValueToMetrics() {
        // Arrange
        String testLine = "Example log entry";
        Map<String, String> parsedLine = Map.of("metric", "cpuLoad", "value", "42");

        // Act
        boolean result = handler.handleLog(testLine, parsedLine);

        // Assert
        assertTrue(result, "Handler should process the log entry.");
        assertEquals(1, apmMetrics.get("cpuLoad").size(), "Metrics should include one entry for 'cpuLoad'.");
        assertEquals(42, (int) apmMetrics.get("cpuLoad").get(0), "The value of 'cpuLoad' should be 42.");
    }

    @Test
    public void handleLog_WhenMetricOrValueMissing_DelegatesToNextHandler() {
        // Arrange
        LogHandler nextHandler = mock(LogHandler.class);
        handler.setNextHandler(nextHandler);

        String testLine = "Another log entry";
        Map<String, String> parsedLine = Map.of("metric", "memoryUsage");

        // Act
        boolean result = handler.handleLog(testLine, parsedLine);

        // Assert
        assertFalse(result, "Handler should not process the log entry without 'value'.");
        verify(nextHandler).handleLog(testLine, parsedLine); // Verify that next handler was called
    }
}
