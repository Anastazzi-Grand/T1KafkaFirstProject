package org.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.model.WeatherCondition;
import org.model.WeatherData;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
        LocalDateTime eventDate = now().minusDays(daysBack).with(LocalTime.MIDNIGHT);

        WeatherData weatherData = new WeatherData(randomTemperature, randomCondition, randomCity, eventDate);

        return weatherData;
    }
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        Producer<String, String> producer = new KafkaProducer<>(props);
        ObjectMapper mapper = new ObjectMapper();
        String topic = "weather-topic";

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(() -> {
            try {
                WeatherData weatherData = generateRandomWeatherData();
                String json = mapper.writeValueAsString(weatherData);

                producer.send(new ProducerRecord<>(topic, json));
                System.out.println("Отправлено: " + weatherData);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 2, TimeUnit.SECONDS);

        // Обработка остановки программы (Ctrl+C)
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Останавливаем producer...");
            scheduler.shutdownNow();
            producer.close();
            System.out.println("Producer остановлен.");
        }));
    }

}
