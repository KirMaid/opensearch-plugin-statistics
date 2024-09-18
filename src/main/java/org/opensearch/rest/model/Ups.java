package org.opensearch.rest.model;

import com.google.gson.annotations.SerializedName;

public class Ups {
    @SerializedName("ups_adv_output_load")
    private Integer  upsAdvOutputLoad;

    @SerializedName("ups_adv_battery_temperature")
    private Integer  upsAdvBatteryTemperature;

    @SerializedName("@timestamp")
    private String timestamp;

    private String host;

    @SerializedName("ups_adv_battery_run_time_remaining")
    private int upsAdvBatteryRunTimeRemaining;

    @SerializedName("ups_adv_output_voltage")
    private Double upsAdvOutputVoltage;
    
    public Integer getUpsAdvOutputLoad() {
        return upsAdvOutputLoad;
    }

    public void setUpsAdvOutputLoad(Integer upsAdvOutputLoad) {
        this.upsAdvOutputLoad = upsAdvOutputLoad;
    }

    public Integer getUpsAdvBatteryTemperature() {
        return upsAdvBatteryTemperature;
    }

    public void setUpsAdvBatteryTemperature(Integer upsAdvBatteryTemperature) {
        this.upsAdvBatteryTemperature = upsAdvBatteryTemperature;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getUpsBatteryRunTimeRemaining() {
        return upsAdvBatteryRunTimeRemaining;
    }

    public void setUpsAdvBatteryRunTimeRemaining(Integer upsAdvBatteryRunTimeRemaining) {
        this.upsAdvBatteryRunTimeRemaining = upsAdvBatteryRunTimeRemaining;
    }

    public Double getUpsAdvOutputVoltage() {
        return upsAdvOutputVoltage;
    }

    public void setUpsAdvOutputVoltage(Double upsAdvOutputVoltage) {
        this.upsAdvOutputVoltage = upsAdvOutputVoltage;
    }
}
