package org.model;

import java.util.Random;

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
