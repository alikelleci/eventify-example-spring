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
  public NewTopic commandsTopic() {
    return TopicBuilder
        .name(CustomerCommand.class.getAnnotation(TopicInfo.class).value())
        .partitions(1)
        .build();
  }

  @Bean
  public NewTopic resultsTopic() {
    return TopicBuilder
        .name(CustomerCommand.class.getAnnotation(TopicInfo.class).value().concat(".results"))
        .partitions(1)
        .build();
  }

  @Bean
  public NewTopic eventsTopic() {
    return TopicBuilder
        .name(CustomerEvent.class.getAnnotation(TopicInfo.class).value())
        .partitions(1)
        .config("retention.ms", "-1")
        .build();
  }

}
