package com.example.eventifyexamplespring.config;

import com.example.eventifyexamplespring.domain.CustomerCommand;
import com.example.eventifyexamplespring.domain.CustomerEvent;
import io.github.alikelleci.eventify.core.common.annotations.TopicInfo;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class TopicConfig {

  @Bean
  public KafkaAdmin admin() {
    Map<String, Object> configs = new HashMap<>();
    configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    return new KafkaAdmin(configs);
  }


  @Bean
  public KafkaAdmin.NewTopics topics() {
    return new KafkaAdmin.NewTopics(
        // BookingSubscription
        commandTopic(CustomerCommand.class),
        commandResultsTopic(CustomerCommand.class),
        eventTopic(CustomerEvent.class)

        // Other aggregate topics...
    );
  }

  private NewTopic commandTopic(Class<?> commandClass) {
    return TopicBuilder.name(commandClass.getAnnotation(TopicInfo.class).value())
        .partitions(10)
        .build();
  }

  private NewTopic commandResultsTopic(Class<?> commandClass) {
    return TopicBuilder
        .name(commandClass.getAnnotation(TopicInfo.class)
            .value() + ".results").partitions(10)
        .build();
  }

  private NewTopic eventTopic(Class<?> eventClass) {
    return TopicBuilder
        .name(eventClass.getAnnotation(TopicInfo.class).value())
        .partitions(10).config("retention.ms", "-1")
        .build();
  }

}
