import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

public class ApplicationLogHandlerTest {
    private ApplicationLogHandler handler;
    private Map<String, Integer> appLogs;

    @BeforeEach
    public void setUp() {
        appLogs = new HashMap<>();
        handler = new ApplicationLogHandler(appLogs);
    }

    @Test
    public void handleLog_WhenLevelPresent_IncrementsLevelCount() {
        // Arrange
        String testLine = "Example log line";
        Map<String, String> parsedLine = Map.of("level", "ERROR");

        // Act
        boolean result = handler.handleLog(testLine, parsedLine);

        // Assert
        assertTrue(result, "Handler should process the log entry.");
        assertEquals(1, appLogs.get("ERROR"), "Error level count should be incremented.");
    }

    @Test
    public void handleLog_WhenLevelNotPresent_DelegatesToNextHandler() {
        // Arrange
        LogHandler nextHandler = mock(LogHandler.class);
        handler.setNextHandler(nextHandler);

        String testLine = "Another log line";
        Map<String, String> parsedLine = Map.of("otherKey", "otherValue");

        // Act
        boolean result = handler.handleLog(testLine, parsedLine);

        // Assert
        assertFalse(result, "Handler should not process the log entry.");
        verify(nextHandler).handleLog(testLine, parsedLine); // Verify that next handler was called
    }
}
