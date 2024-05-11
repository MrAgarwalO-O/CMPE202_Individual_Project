import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RequestDataTest {

    private RequestData requestData;

    @BeforeEach
    void setUp() {
        requestData = new RequestData();
    }

    @Test
    void testAddResponseTime() {
        requestData.addResponseTime(100);
        requestData.addResponseTime(200);
        Map<String, Object> result = requestData.aggregate();
        Map responseTimes = (Map) result.get("response_times");
        assertEquals(100, responseTimes.get("min"));
        assertEquals(200, responseTimes.get("max"));
    }

    @Test
    void testIncrementStatusCodeCategory() {
        requestData.incrementStatusCodeCategory("200");
        requestData.incrementStatusCodeCategory("404");
        requestData.incrementStatusCodeCategory("500");
        Map<String, Object> result = requestData.aggregate();
        Map statusCodes = (Map) result.get("status_codes");
        assertEquals(1, statusCodes.get("2XX"));
        assertEquals(1, statusCodes.get("4XX"));
        assertEquals(1, statusCodes.get("5XX"));
    }

    @Test
    void testAggregateEmptyResponseTimes() {
        Map<String, Object> result = requestData.aggregate();
        assertTrue(((Map) result.get("response_times")).isEmpty());
    }

    @Test
    void testAggregateCalculatesPercentilesCorrectly() {
        for (int i = 1; i <= 100; i++) {
            requestData.addResponseTime(i);
        }
        Map<String, Object> result = requestData.aggregate();
        Map responseTimes = (Map) result.get("response_times");
        assertEquals(1, responseTimes.get("min"));
        assertEquals(50, responseTimes.get("50_percentile"));
        assertEquals(90, responseTimes.get("90_percentile"));
        assertEquals(95, responseTimes.get("95_percentile"));
        assertEquals(99, responseTimes.get("99_percentile"));
        assertEquals(100, responseTimes.get("max"));
    }
}
