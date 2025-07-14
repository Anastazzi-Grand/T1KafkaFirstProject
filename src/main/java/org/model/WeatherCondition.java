package org.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Random;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum WeatherCondition {
    SUNNY("солнечно"),
    CLOUDY("облачно"),
    RAINY("дождь");

    private final String displayName;


    WeatherCondition(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static WeatherCondition getRandom() {
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }
}
