package com.example.eventifyexamplespring;

import io.github.alikelleci.eventify.core.Eventify;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
//@Component
@EnableScheduling
public class LagMonitor2 {

  @Autowired
  private Eventify eventify;

  @Scheduled(fixedRate = 5000)
  public void reportMetrics() {
    log.info("Monitoring lag");
    Map<MetricName, ? extends Metric> metrics = eventify.getKafkaStreams().metrics();
    metrics.forEach((metricName, metric) -> {
      if (metricName.name().equals("records-lag")) {
        Map<String, String> tags = metricName.tags();
        log.info("topic: {}, partition: {}, lag: {}", tags.get("topic"), tags.get("partition"), metric.metricValue());
      }
    });

  }
}
