package com.example.eventifyexamplespring;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.*;
import java.util.concurrent.*;

@Slf4j
public class LagMonitor {

  private final AdminClient adminClient;
  private final Consumer<String, String> consumer;
  private final long intervalMs;
  private final ExecutorService executorService;

  public LagMonitor(AdminClient adminClient, Consumer<String, String> consumer, long intervalMs) {
    this.adminClient = adminClient;
    this.consumer = consumer;
    this.intervalMs = intervalMs;
    this.executorService = Executors.newFixedThreadPool(10); // Thread pool for parallel processing
  }

  public void startMonitoring() {
    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    // Schedule lag monitoring at fixed intervals
    scheduler.scheduleAtFixedRate(this::monitorConsumerGroupLag, 0, intervalMs, TimeUnit.MILLISECONDS);

    // Add a shutdown hook to clean up resources
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      log.info("Shutting down LagMonitor...");
      scheduler.shutdown();
      executorService.shutdown();
      adminClient.close();
      consumer.close();
    }));
  }

  private void monitorConsumerGroupLag() {
    try {
      // Fetch all consumer groups
      Collection<ConsumerGroupListing> consumerGroups = adminClient.listConsumerGroups().all().get();

      // Submit tasks to process each consumer group in parallel
      for (ConsumerGroupListing group : consumerGroups) {
        executorService.submit(() -> fetchAndLogLagForGroup(group.groupId()));
      }
    } catch (Exception e) {
      log.error("Error fetching consumer groups or monitoring lag", e);
    }
  }

  private void fetchAndLogLagForGroup(String groupId) {
    try {
      // Get consumer group offsets
      Map<TopicPartition, OffsetAndMetadata> groupOffsets = adminClient.listConsumerGroupOffsets(groupId)
          .partitionsToOffsetAndMetadata()
          .get();

      Map<String, Long> topicLagMap = new HashMap<>();

      // Calculate lag for each topic partition
      for (TopicPartition partition : groupOffsets.keySet()) {
        long currentOffset = groupOffsets.get(partition).offset();
        long endOffset = consumer.endOffsets(List.of(partition)).getOrDefault(partition, 0L);

        long lag = endOffset - currentOffset;

        // Log partition lag
        log.info("Consumer Group: {}, Partition: {}, Current Offset: {}, End Offset: {}, Lag: {}", groupId, partition, currentOffset, endOffset, lag);

        // Sum up lag per topic
        topicLagMap.merge(partition.topic(), lag, Long::sum);
      }

      // Log total lag per topic
      topicLagMap.forEach((topic, totalLag) -> log.info("Consumer Group: {}, Topic: {}, Total Lag: {}", groupId, topic, totalLag));

    } catch (Exception e) {
      log.error("Error fetching lag for group: {}", groupId, e);
    }
  }

  public static void main(String[] args) {
    AdminClient adminClient = createAdminClient();
    Consumer<String, String> consumer = createConsumer();

    LagMonitor lagMonitor = new LagMonitor(adminClient, consumer, 3000); // Check every 3 seconds
    lagMonitor.startMonitoring();
  }

  private static AdminClient createAdminClient() {
    Properties properties = new Properties();
    properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    return AdminClient.create(properties);
  }

  private static Consumer<String, String> createConsumer() {
    Properties properties = new Properties();
    properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    properties.put(ConsumerConfig.GROUP_ID_CONFIG, "monitoring-consumer");
    properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    properties.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
    properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    return new KafkaConsumer<>(properties);
  }
}
