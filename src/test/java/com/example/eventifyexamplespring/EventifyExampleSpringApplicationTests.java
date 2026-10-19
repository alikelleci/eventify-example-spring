package com.example.eventifyexamplespring;

import com.example.eventifyexamplespring.order.OrderCommand;
import com.example.eventifyexamplespring.order.OrderCommand.ConfirmOrder;
import com.example.eventifyexamplespring.order.OrderCommand.PlaceOrder;
import com.example.eventifyexamplespring.order.OrderCommandHandler;
import com.example.eventifyexamplespring.order.OrderEvent;
import com.example.eventifyexamplespring.order.OrderEvent.OrderConfirmed;
import com.example.eventifyexamplespring.order.OrderEvent.OrderPlaced;
import com.example.eventifyexamplespring.order.OrderEventSourcingHandler;
import io.github.alikelleci.eventify.core.Eventify;
import io.github.alikelleci.eventify.core.command.Command;
import io.github.alikelleci.eventify.core.event.Event;
import io.github.alikelleci.eventify.core.message.annotation.Topic;
import io.github.alikelleci.eventify.core.serialization.JsonDeserializer;
import io.github.alikelleci.eventify.core.serialization.JsonSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.TopologyTestDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EventifyExampleSpringApplicationTests {

  private TopologyTestDriver testDriver;
  private TestInputTopic<String, Command> commands;
  private TestOutputTopic<String, Event> events;

  @BeforeEach
  void setUp() {
    Eventify eventify = Eventify.builder()
        .registerHandler(new OrderCommandHandler())
        .registerHandler(new OrderEventSourcingHandler())
        .build();

    testDriver = new TopologyTestDriver(eventify.topology());

    commands = testDriver.createInputTopic(
        OrderCommand.class.getAnnotation(Topic.class).value(),
        new StringSerializer(),
        new JsonSerializer<>());

    events = testDriver.createOutputTopic(
        OrderEvent.class.getAnnotation(Topic.class).value(),
        new StringDeserializer(),
        new JsonDeserializer<>(Event.class));
  }

  @AfterEach
  void tearDown() {
    if (testDriver != null) {
      testDriver.close();
    }
  }


  @Test
  void ShipOrderTest() {
    Command command = Command.builder()
        .payload(PlaceOrder.builder()
            .id("order-1")
            .customer("John Doe")
            .shippingAddress("Some Street 212")
            .couponCode("HMX004")
            .build())
        .build();

    // publish command to topic with aggregateId as key!
    commands.pipeInput(command.getAggregateId(), command);

    command = Command.builder()
        .payload(ConfirmOrder.builder()
            .id("order-1")
            .build())
        .build();

    // publish command to topic with aggregateId as key!
    commands.pipeInput(command.getAggregateId(), command);

    // read events
    List<Event> result = events.readValuesToList();

    // assert
    assertThat(result).hasSize(2);
    assertThat(result.get(0).getPayload()).isInstanceOf(OrderPlaced.class);
    assertThat(result.get(1).getPayload()).isInstanceOf(OrderConfirmed.class);
  }

}
