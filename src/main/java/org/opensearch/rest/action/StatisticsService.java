package org.opensearch.rest.action;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.opensearch.env.Environment;
import org.opensearch.rest.model.StatisticsResponse;
import org.opensearch.rest.model.Ups;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class StatisticsService {
    private final Environment environment;

    public StatisticsService(Environment environment) {
        this.environment = environment;
    }

    public StatisticsResponse processFile(String filePath) throws IOException {
        Path resolvedPath = environment.configDir().resolve(filePath);
        Gson gson = new Gson();
        try (BufferedReader reader = Files.newBufferedReader(resolvedPath, StandardCharsets.UTF_8)) {
            Type listType = new TypeToken<ArrayList<Ups>>() {}.getType();
            List<Ups> statuses = gson.fromJson(reader, listType);

            double avgRunTime = calculateAverageRunTime(statuses);
            int maxOutputVoltage = findMaxOutputVoltage(statuses);
            List<String> hosts = extractHosts(statuses);

            return new StatisticsResponse(avgRunTime, maxOutputVoltage, hosts);
        }
    }

    private double calculateAverageRunTime(List<Ups> statuses) {
        return statuses.stream()
                .mapToInt(Ups::getUpsAdvBatteryRunTimeRemaining)
                .average()
                .orElse(0.0);
    }

    private int findMaxOutputVoltage(List<Ups> statuses) {
        return statuses.stream()
                .mapToInt(Ups::getUpsAdvOutputVoltage)
                .max()
                .orElse(0);
    }

    private List<String> extractHosts(List<Ups> statuses) {
        List<String> hosts = new ArrayList<>();
        for (Ups status : statuses) {
            hosts.add(status.getHost());
        }
        return hosts;
    }
}
