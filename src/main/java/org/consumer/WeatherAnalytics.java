package org.consumer;

import org.model.WeatherCondition;
import org.model.WeatherData;

import java.util.*;

public class WeatherAnalytics {

    private final Map<String, List<WeatherData>> weatherByCity = new HashMap<>();

    public void process(WeatherData data) {
        // Добавляем данные в хранилище
        weatherByCity.computeIfAbsent(data.getCity(), k -> new ArrayList<>()).add(data);
    }

    // 1. Самое дождливое место
    public void printRainiestCity() {
        Map<String, Long> rainCountByCity = new HashMap<>();

        for (var entry : weatherByCity.entrySet()) {
            String city = entry.getKey();
            long rainCount = entry.getValue().stream()
                    .filter(d -> d.getCondition() == WeatherCondition.RAINY)
                    .count();
            rainCountByCity.put(city, rainCount);
        }

        rainCountByCity.forEach((city, count) ->
                System.out.println("Количество дождливых дней в " + city + ": " + count));

        String rainiest = rainCountByCity.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Нет данных");

        System.out.println("Самое дождливое место: " + rainiest + ", дождливых дней: " + rainCountByCity.get(rainiest));
    }

    // 2. Самая жаркая погода
    public void printHottestWeather() {
        WeatherData hottest = weatherByCity.values().stream()
                .flatMap(List::stream)
                .max(Comparator.comparingDouble(WeatherData::getTemperature))
                .orElse(null);

        if (hottest != null) {
            System.out.println("Самая жаркая погода: " + hottest.getTemperature() +
                    "°C в городе " + hottest.getCity() + " (" + hottest.getCondition() + ")");
        } else {
            System.out.println("Нет данных о температуре.");
        }
    }

    // 3. Самая низкая средняя температура
    public void printColdestAverageCity() {
        Map<String, Double> avgTempByCity = new HashMap<>();

        for (var entry : weatherByCity.entrySet()) {
            String city = entry.getKey();
            List<WeatherData> dataList = entry.getValue();

            double avg = dataList.stream()
                    .mapToDouble(WeatherData::getTemperature)
                    .average()
                    .orElse(0.0);

            avgTempByCity.put(city, avg);
        }

        avgTempByCity.forEach((city, avg) ->
                System.out.println("Средняя температура в " + city + ": " + String.format("%.2f", avg)));

        String coldest = avgTempByCity.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Нет данных");

        System.out.println("Самая низкая средняя температура в городе: " + coldest +
                ", средняя температура: " + String.format("%.2f", avgTempByCity.get(coldest)));
    }
}
