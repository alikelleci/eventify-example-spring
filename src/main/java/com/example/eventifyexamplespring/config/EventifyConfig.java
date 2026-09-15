package com.example.eventifyexamplespring.config;

import io.github.alikelleci.eventify.console.plugin.EventifyConsolePlugin;
import io.github.alikelleci.eventify.core.Eventify;
import io.github.alikelleci.eventify.core.messaging.commandhandling.gateway.CommandGateway;
import io.github.alikelleci.eventify.core.messaging.eventhandling.gateway.EventGateway;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class EventifyConfig {

  @Value("${spring.application.name}")
  private String applicationName;

  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;

  @Value("${app.server-endpoint}")
  private String serverEndpoint;

  @Value("${app.state-dir:}")
  private String stateDir;

  @Bean
  public Eventify eventify() {
    Properties streamsConfig = new Properties();
    streamsConfig.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationName);
    streamsConfig.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    streamsConfig.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 10_000);
    streamsConfig.put(StreamsConfig.APPLICATION_SERVER_CONFIG, serverEndpoint);
    if (!stateDir.isBlank()) {
      streamsConfig.put(StreamsConfig.STATE_DIR_CONFIG, stateDir);
    }

    return Eventify.builder()
        .streamsConfig(streamsConfig)
        .registerPlugin(EventifyConsolePlugin.builder()
            .url("http://localhost:4040")
            .build())
        .build();
  }

  @Bean
  public CommandGateway commandGateway() {
    Properties producerConfig = new Properties();
    producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

    return CommandGateway.builder()
        .producerConfig(producerConfig)
        .replyTopic("my-reply-channel")
        .build();
  }

  @Bean
  public EventGateway eventGateway() {
    Properties producerConfig = new Properties();
    producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

    return EventGateway.builder()
        .producerConfig(producerConfig)
        .build();
  }
}
