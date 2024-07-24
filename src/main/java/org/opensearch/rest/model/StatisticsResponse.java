package org.opensearch.rest.model;

import java.util.List;

public class StatisticsResponse {
    private final double avgRunTime;
    private final int maxOutputVoltage;
    private final List<String> hosts;

    public StatisticsResponse(double avgRunTime, int maxOutputVoltage, List<String> hosts) {
        this.avgRunTime = avgRunTime;
        this.maxOutputVoltage = maxOutputVoltage;
        this.hosts = hosts;
    }

    public double getAvgRunTime() {
        return avgRunTime;
    }

    public int getMaxOutputVoltage() {
        return maxOutputVoltage;
    }

    public List<String> getHosts() {
        return hosts;
    }
}