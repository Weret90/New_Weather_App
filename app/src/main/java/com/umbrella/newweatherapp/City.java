package com.umbrella.newweatherapp;

import java.io.Serializable;

public class City implements Serializable {

    private final String name;
    private final String status;
    private final String temperature;
    private final String tomorrowDay;
    private final String tomorrowDayStatus;
    private final String tomorrowDayTemperature;
    private final String afterTomorrowDay;
    private final String afterTomorrowDayStatus;
    private final String afterTomorrowDayTemperature;

    public City(String name, String status, String temperature, String tomorrowDay, String tomorrowDayStatus, String tomorrowDayTemperature, String afterTomorrowDay, String afterTomorrowDayStatus, String afterTomorrowDayTemperature) {
        this.name = name;
        this.status = status;
        this.temperature = temperature;
        this.tomorrowDay = tomorrowDay;
        this.tomorrowDayStatus = tomorrowDayStatus;
        this.tomorrowDayTemperature = tomorrowDayTemperature;
        this.afterTomorrowDay = afterTomorrowDay;
        this.afterTomorrowDayStatus = afterTomorrowDayStatus;
        this.afterTomorrowDayTemperature = afterTomorrowDayTemperature;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getTomorrowDay() {
        return tomorrowDay;
    }

    public String getTomorrowDayStatus() {
        return tomorrowDayStatus;
    }

    public String getTomorrowDayTemperature() {
        return tomorrowDayTemperature;
    }

    public String getAfterTomorrowDay() {
        return afterTomorrowDay;
    }

    public String getAfterTomorrowDayStatus() {
        return afterTomorrowDayStatus;
    }

    public String getAfterTomorrowDayTemperature() {
        return afterTomorrowDayTemperature;
    }
}
