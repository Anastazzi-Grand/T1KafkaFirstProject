package org.producer;

import org.model.WeatherCondition;
import org.model.WeatherData;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

import static java.time.LocalDateTime.now;

public class WeatherProducer {
    private static final List<String> CITIES = List.of(
            "Москва", "Санкт-Петербург", "Екатеринбург",
            "Новосибирск", "Владивосток", "Магадан",
            "Чукотка", "Тюмень"
    );

    public static WeatherData generateRandomWeatherData() {
        Random random = new Random();
        String randomCity = CITIES.get(random.nextInt(CITIES.size()));
        int randomTemperature = random.nextInt(0, 35);
        WeatherCondition randomCondition = WeatherCondition.getRandom();

        int daysBack = random.nextInt(7);
        LocalDateTime eventDate = now().minusDays(daysBack).with(LocalTime.now().withNano(0));

        WeatherData weatherData = new WeatherData(randomTemperature, randomCondition, randomCity, eventDate);

        return weatherData;
    }
    public static void main(String[] args) {

        WeatherData weatherData = generateRandomWeatherData();

        System.out.println(weatherData);
    }

}
