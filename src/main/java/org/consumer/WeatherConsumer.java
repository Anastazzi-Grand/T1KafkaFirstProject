package org.consumer;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.model.WeatherData;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WeatherConsumer {

    private static Properties createConsumerConfig() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "weather-consumer-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return props;
    }

    private static void readMessages(KafkaConsumer<String, String> consumer, WeatherAnalytics analytics) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        try {
            while (true) {
                var records = consumer.poll(Duration.ofMillis(3000));
                for (ConsumerRecord<String, String> record : records) {
                    try {
                        WeatherData data = mapper.readValue(record.value(), WeatherData.class);
                        analytics.process(data);
                    } catch (Exception e) {
                        System.err.println("Ошибка десериализации: " + e.getMessage());
                    }
                }
            }
        } finally {
            consumer.close();
        }
    }

    private static void startStatisticsReporting(WeatherAnalytics analytics, ScheduledExecutorService scheduler) {
        scheduler.scheduleAtFixedRate(() -> {
            System.out.println("\n--- Актуальная статистика ---");
            analytics.printRainiestCity();
            analytics.printHottestWeather();
            analytics.printColdestAverageCity();
        }, 0, 5, TimeUnit.SECONDS);
    }

    public static void main(String[] args) {
        Properties props = createConsumerConfig();
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        WeatherAnalytics analytics = new WeatherAnalytics();

        consumer.subscribe(Collections.singletonList("weather-topic"));

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        startStatisticsReporting(analytics, scheduler);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Останавливаем consumer...");
            scheduler.shutdownNow();
            consumer.close();
            System.out.println("Consumer остановлен.");
        }));

        readMessages(consumer, analytics);
    }

}
