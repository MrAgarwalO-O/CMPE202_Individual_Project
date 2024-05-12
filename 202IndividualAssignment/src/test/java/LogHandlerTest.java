import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class LogHandlerTest {
    
    @Test
    public void setNextHandler_SetsNextLogHandler_Correctly() {
       
        LogHandler handler = mock(LogHandler.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
        LogHandler nextHandler = mock(LogHandler.class);

        handler.setNextHandler(nextHandler);

        assertEquals(nextHandler, handler.nextHandler, "Next handler should be set correctly.");
    }

    @Test
    public void handleLog_ChainHandling_CallsNextHandler() {
        
        LogHandler handler = new LogHandler() {
            @Override
            public boolean handleLog(String line, Map<String, String> parsedLine) {
                
                if (nextHandler != null) {
                    return nextHandler.handleLog(line, parsedLine);
                }
                return false;
            }
        };
        LogHandler nextHandler = mock(LogHandler.class);
        handler.setNextHandler(nextHandler);

        String testLine = "test line";
        Map<String, String> testParsedLine = new HashMap<>();
        testParsedLine.put("key", "value");

        handler.handleLog(testLine, testParsedLine);

        verify(nextHandler).handleLog(testLine, testParsedLine);
    }
}
