package org.opensearch.rest.model;

import com.google.gson.annotations.SerializedName;

public class Ups {
    @SerializedName("ups_adv_output_load")
    private int upsAdvOutputLoad;

    @SerializedName("ups_adv_battery_temperature")
    private int upsAdvBatteryTemperature;

    @SerializedName("@timestamp")
    private String timestamp;

    private String host;

    @SerializedName("ups_adv_battery_run_time_remaining")
    private int upsAdvBatteryRunTimeRemaining;

    @SerializedName("ups_adv_output_voltage")
    private int upsAdvOutputVoltage;

    public int getUpsAdvOutputLoad() {
        return upsAdvOutputLoad;
    }

    public void setUpsAdvOutputLoad(int upsAdvOutputLoad) {
        this.upsAdvOutputLoad = upsAdvOutputLoad;
    }

    public int getUpsAdvBatteryTemperature() {
        return upsAdvBatteryTemperature;
    }

    public void setUpsAdvBatteryTemperature(int upsAdvBatteryTemperature) {
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

    public int getUpsAdvBatteryRunTimeRemaining() {
        return upsAdvBatteryRunTimeRemaining;
    }

    public void setUpsAdvBatteryRunTimeRemaining(int upsAdvBatteryRunTimeRemaining) {
        this.upsAdvBatteryRunTimeRemaining = upsAdvBatteryRunTimeRemaining;
    }

    public int getUpsAdvOutputVoltage() {
        return upsAdvOutputVoltage;
    }

    public void setUpsAdvOutputVoltage(int upsAdvOutputVoltage) {
        this.upsAdvOutputVoltage = upsAdvOutputVoltage;
    }
}
