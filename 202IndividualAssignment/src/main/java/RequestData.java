import java.util.*;

public class RequestData {
    private List<Integer> responseTimes = new ArrayList<>();
    private Map<String, Integer> statusCodeCounts = new HashMap<>();

    public void addResponseTime(int time) {
        responseTimes.add(time);
    }

    public void incrementStatusCodeCategory(String status) {
        String category = status.substring(0, 1) + "XX";
        statusCodeCounts.put(category, statusCodeCounts.getOrDefault(category, 0) + 1);
    }

    public Map<String, Object> aggregate() {
        if (responseTimes.isEmpty()) {
            return Map.of(
                "response_times", new LinkedHashMap<>(),
                "status_codes", new HashMap<>(statusCodeCounts)
            );
        }

        Collections.sort(responseTimes);
        Map<String, Object> responseTimesMap = new LinkedHashMap<>();
        responseTimesMap.put("min", responseTimes.get(0));
        responseTimesMap.put("50_percentile", percentile(50));
        responseTimesMap.put("90_percentile", percentile(90));
        responseTimesMap.put("95_percentile", percentile(95));
        responseTimesMap.put("99_percentile", percentile(99));
        responseTimesMap.put("max", responseTimes.get(responseTimes.size() - 1));

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("response_times", responseTimesMap);
        result.put("status_codes", new HashMap<>(statusCodeCounts));
        return result;
    }

    private int percentile(int p) {
        int index = (int) Math.ceil(p / 100.0 * responseTimes.size()) - 1;
        return responseTimes.get(index);
    }
}
