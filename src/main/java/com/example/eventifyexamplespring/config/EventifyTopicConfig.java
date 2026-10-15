package com.example.eventifyexamplespring.config;

import com.example.eventifyexamplespring.order.OrderCommand;
import com.example.eventifyexamplespring.order.OrderEvent;
import io.github.alikelleci.eventify.core.message.annotation.Topic;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaAdmin.NewTopics;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class EventifyTopicConfig {

  @Bean
  public KafkaAdmin admin() {
    Map<String, Object> configs = new HashMap<>();
    configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    return new KafkaAdmin(configs);
  }


  @Bean
  public NewTopics topics() {
    return new NewTopics(
        commandTopic(OrderCommand.class),
        commandResultsTopic(OrderCommand.class),
        eventTopic(OrderEvent.class)
    );
  }

  private NewTopic commandTopic(Class<?> commandClass) {
    return TopicBuilder
        .name(commandClass.getAnnotation(Topic.class).value())
        .partitions(10)
        .build();
  }

  private NewTopic commandResultsTopic(Class<?> commandClass) {
    return TopicBuilder
        .name(commandClass.getAnnotation(Topic.class).value() + ".results")
        .partitions(10)
        .build();
  }

  private NewTopic eventTopic(Class<?> eventClass) {
    return TopicBuilder
        .name(eventClass.getAnnotation(Topic.class).value())
        .partitions(10).config("retention.ms", "-1")
        .build();
  }

}
