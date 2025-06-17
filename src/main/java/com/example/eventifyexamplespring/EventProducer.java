package com.example.eventifyexamplespring;

import com.example.eventifyexamplespring.domain.CustomerEvent.CreditsAdded;
import com.example.eventifyexamplespring.domain.CustomerEvent.CustomerCreated;
import io.github.alikelleci.eventify.core.messaging.Message;
import io.github.alikelleci.eventify.core.messaging.eventhandling.Event;
import io.github.alikelleci.eventify.core.support.serialization.json.JsonSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Slf4j
public class EventProducer {

  public static final Producer<String, Message> producer = createProducer();

  public static final int NUMBER_OF_AGGREGATES = 1000;
  public static final int NUMBER_OF_EVENTS_PER_AGGREGATE = 1000;

  public static void main(String[] args) {
    generateEvents(NUMBER_OF_AGGREGATES, NUMBER_OF_EVENTS_PER_AGGREGATE);
  }

  private static void generateEvents(int numberOfAggregates, int numberOfEventsPerAggregate) {
    String topic = "example-app-event-store-changelog";

    log.info("Generating events...");
    for (int i = 1; i <= numberOfAggregates; i++) {
      String aggregateId = "cust-" + i;

      generateEventsFor(aggregateId, numberOfEventsPerAggregate, true)
          .forEach(event -> producer.send(new ProducerRecord<>(topic, event.getId(), event)));
    }
    producer.flush();
    log.info("Number of events generated: {}", numberOfAggregates * numberOfEventsPerAggregate);
  }

  public static List<Event> generateEventsFor(String aggregateId, int numEvents, boolean includeCreation) {
    List<Event> list = new ArrayList<>();
    for (int i = 1; i <= numEvents; i++) {
      Object payload;
      if (i == 1 && includeCreation) {
        payload = CustomerCreated.builder()
            .id(aggregateId)
            .firstName("John")
            .firstName("Doe")
            .credits(100)
            .build();
      } else {
        payload = CreditsAdded.builder()
            .id(aggregateId)
            .amount(1)
            .build();
      }
      list.add(Event.builder()
          .payload(payload)
          .build());
    }
    return list;
  }

  public static Producer<String, Message> createProducer() {
    Properties properties = new Properties();
    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    properties.put(ProducerConfig.ACKS_CONFIG, "all");
    properties.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE);
    properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
    properties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "PLAINTEXT");

    return new KafkaProducer<>(properties,
        new StringSerializer(),
        new JsonSerializer<>());
  }
}
