// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;

// import java.util.HashMap;
// import java.util.Map;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

// public class RequestLogHandlerTest {

//     private RequestLogHandler handler;
//     private Map<String, RequestData> requestLogs;
//     private LogHandler nextHandler;

//     @BeforeEach
//     public void setup() {
//         requestLogs = new HashMap<>();
//         handler = new RequestLogHandler(requestLogs);
//         nextHandler = mock(LogHandler.class);
//         handler.setNextHandler(nextHandler);
//     }

//     @Test
//     public void testHandleLogWithValidRequestUrl() {
        
//         String line = "Sample log line";
//         Map<String, String> parsedLine = Map.of(
//             "request_url", "http://example.com",
//             "response_time_ms", "200",
//             "response_status", "200");

        
//         boolean result = handler.handleLog(line, parsedLine);

        
//         assertTrue(result, "Log handling should succeed");
//         assertTrue(requestLogs.containsKey("http://example.com"), "Request URL should be logged");
//         RequestData requestData = requestLogs.get("http://example.com");
//         assertEquals(200, requestData.getTotalResponseTime(), "Response time should be recorded correctly");
//         assertEquals(1, requestData.getStatusCodeCount("200"), "Status code count should be incremented");
//     }

//     @Test
//     public void testHandleLogWithMissingRequestUrl() {
        
//         String line = "Another sample log line";
//         Map<String, String> parsedLine = Map.of("response_time_ms", "300");

        
//         when(nextHandler.handleLog(line, parsedLine)).thenReturn(true);

       
//         boolean result = handler.handleLog(line, parsedLine);

        
//         assertFalse(requestLogs.containsKey("http://example.com"), "Request URL should not be logged");
//         assertTrue(result, "Next handler should handle the log");
//         verify(nextHandler).handleLog(line, parsedLine);
//     }

//     @Test
//     public void testHandleLogWithInvalidResponseTime() {
      
//         String line = "Error log line";
//         Map<String, String> parsedLine = Map.of(
//             "request_url", "http://example.com",
//             "response_time_ms", "invalid_time",
//             "response_status", "200");

       
//         assertThrows(NumberFormatException.class, () -> {
//             handler.handleLog(line, parsedLine);
//         }, "Should throw NumberFormatException due to invalid response time");
//     }
// }
