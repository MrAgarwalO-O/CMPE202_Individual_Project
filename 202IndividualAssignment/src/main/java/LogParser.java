import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class LogParser {
    private static final String APM_JSON = "apm.json";
    private static final String APPLICATION_JSON = "application.json";
    private static final String REQUEST_JSON = "request.json";
    private static final String INPUT_FILE_NAME = "input.txt";

    public static void main(String[] args) throws IOException {
        // if (args.length < 2 || !"--file".equals(args[0])) {
        //     System.out.println("Usage: --file <filename.txt>");
        //     return;
        // }

        String fileName = INPUT_FILE_NAME;
        Map<String, List<Integer>> apmMetrics = new HashMap<>();
        Map<String, Integer> appLogs = new HashMap<>();
        Map<String, RequestData> requestLogs = new HashMap<>();

        APMLogHandler apmHandler = new APMLogHandler(apmMetrics);
        ApplicationLogHandler appHandler = new ApplicationLogHandler(appLogs);
        RequestLogHandler requestHandler = new RequestLogHandler(requestLogs);

        apmHandler.setNextHandler(appHandler);
        appHandler.setNextHandler(requestHandler);

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Map<String, String> parsedLine = parseLine(line);
                if (!apmHandler.handleLog(line, parsedLine) && 
                    !appHandler.handleLog(line, parsedLine) &&
                    !requestHandler.handleLog(line, parsedLine)) {
                    System.out.println("Unmatched log line: " + line);
                }
            }
        }

        writeToJsonFile(APM_JSON, aggregateApmMetrics(apmMetrics));
        writeToJsonFile(APPLICATION_JSON, appLogs);
        writeToJsonFile(REQUEST_JSON, aggregateRequestLogs(requestLogs));
    }

    static Map<String, String> parseLine(String line) {
        return Arrays.stream(line.split(" "))
                .map(kv -> kv.split("="))
                .filter(kv -> kv.length == 2)
                .collect(Collectors.toMap(kv -> kv[0], kv -> kv[1]));
    }

    private static void writeToJsonFile(String fileName, Object data) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(Paths.get(fileName).toFile(), data);
    }

    static Map<String, Map<String, Double>> aggregateApmMetrics(Map<String, List<Integer>> metrics) {
        Map<String, Map<String, Double>> aggregated = new HashMap<>();
        metrics.forEach((key, values) -> {
            Collections.sort(values);
            double min = values.get(0);
            double max = values.get(values.size() - 1);
            double median = (values.size() % 2 == 0) ? 
                (values.get(values.size() / 2 - 1) + values.get(values.size() / 2)) / 2.0 :
                values.get(values.size() / 2);
            double average = values.stream().mapToInt(Integer::intValue).average().orElse(0.0);
            Map<String, Double> orderedMap = new LinkedHashMap<>(); 
            orderedMap.put("minimum", min);
            orderedMap.put("median", median);
            orderedMap.put("average", average);
            orderedMap.put("max", max);
            aggregated.put(key, orderedMap);
        });
        return aggregated;
    }

    private static Map<String, Object> aggregateRequestLogs(Map<String, RequestData> logs) {
        Map<String, Object> aggregated = new HashMap<>();
        logs.forEach((url, data) -> aggregated.put(url, data.aggregate()));
        return aggregated;
    }
}
