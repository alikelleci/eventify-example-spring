package com.example.eventifyexamplespring;

import io.github.alikelleci.eventify.core.messaging.Message;
import io.github.alikelleci.eventify.core.support.serialization.json.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.CooperativeStickyAssignor;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

@Slf4j
public class EventConsumer {

  public static final Consumer<String, Message> consumer = createConsumer();

  public static final String TOPIC = "example-app-event-store-changelog";
//  public static final String TOPIC = "events.customer";


  public static void main(String[] args) throws InterruptedException {
    consumer.subscribe(List.of(TOPIC));

    int consumedMessages = 0;
    while (consumedMessages < 1000) {
      ConsumerRecords<String, Message> records = consumer.poll(Duration.ofMillis(100));
      for (ConsumerRecord<String, Message> record : records) {
        // Optionally print or log the consumed messages
        consumedMessages++;
      }
    }
  }

  public static Consumer<String, Message> createConsumer() {
    Properties properties = new Properties();
    properties.put(ConsumerConfig.GROUP_ID_CONFIG, "example-consumer");
    properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    properties.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
    properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    properties.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, CooperativeStickyAssignor.class.getName());
    properties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "PLAINTEXT");

    return new KafkaConsumer<>(properties,
        new StringDeserializer(),
        new JsonDeserializer<>(Message.class));
  }
}
