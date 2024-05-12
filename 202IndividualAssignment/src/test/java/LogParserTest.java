import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

public class LogParserTest {

    @Test
    public void testParseLine() {
        String testLine = "key1=value1 key2=value2";
        Map<String, String> output = LogParser.parseLine(testLine);
        assertEquals(2, output.size(), "Expected two entries in the map.");
        assertEquals("value1", output.get("key1"), "The value of 'key1' should be 'value1'.");
        assertEquals("value2", output.get("key2"), "The value of 'key2' should be 'value2'.");
    }

    @Test
    public void testAggregateApmMetrics() {
        Map<String, List<Integer>> inputMetrics = new HashMap<>();
        inputMetrics.put("responseTime", Arrays.asList(10, 20, 30, 40, 50));

        Map<String, Map<String, Double>> aggregatedMetrics = LogParser.aggregateApmMetrics(inputMetrics);
        assertNotNull(aggregatedMetrics.get("responseTime"), "Aggregated metrics should contain 'responseTime' data.");

        Map<String, Double> responseTimeMetrics = aggregatedMetrics.get("responseTime");
        assertEquals(10.0, responseTimeMetrics.get("minimum"), "Minimum should be 10");
        assertEquals(30.0, responseTimeMetrics.get("median"), "Median should be 30");
        assertEquals(30.0, responseTimeMetrics.get("average"), "Average should be 30");
        assertEquals(50.0, responseTimeMetrics.get("max"), "Max should be 50");
    }
}
