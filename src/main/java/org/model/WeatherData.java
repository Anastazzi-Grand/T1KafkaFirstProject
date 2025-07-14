package org.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class WeatherData {
    private int temperature;
    private WeatherCondition condition;
    private String city;
    private LocalDateTime date;

    public WeatherData(int temperature, WeatherCondition condition, String city, LocalDateTime date) {
        this.temperature = temperature;
        this.condition = condition;
        this.city = city;
        this.date = date;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public WeatherCondition getCondition() {
        return condition;
    }

    public void setCondition(WeatherCondition condition) {
        this.condition = condition;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "WeatherData{" +
                "temperature=" + temperature +
                ", condition=" + condition.getDisplayName() +
                ", city='" + city + '\'' +
                ", date=" + date +
                '}';
    }
}
