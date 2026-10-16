package com.example.eventifyexamplespring.order;

import com.example.eventifyexamplespring.order.OrderCommand.CancelOrder;
import com.example.eventifyexamplespring.order.OrderCommand.ConfirmOrder;
import com.example.eventifyexamplespring.order.OrderCommand.DeliverOrder;
import com.example.eventifyexamplespring.order.OrderCommand.PlaceOrder;
import com.example.eventifyexamplespring.order.OrderCommand.ShipOrder;
import com.github.javafaker.Faker;
import io.github.alikelleci.eventify.core.command.CommandResult.Success;
import io.github.alikelleci.eventify.core.command.gateway.CommandGateway;
import io.github.alikelleci.eventify.core.event.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping(value = "/orders")
public class OrderController {

  @Autowired
  private CommandGateway commandGateway;
  private Faker faker = new Faker();

  @GetMapping(value = "/place/{id}")
  public CompletableFuture<List<Event>> place(@PathVariable String id) {
    return commandGateway.send(PlaceOrder.builder()
        .id(id)
        .customer(faker.name().fullName())
        .shippingAddress(faker.address().fullAddress())
        .couponCode(faker.commerce().promotionCode(4))
        .build());
  }

  @GetMapping(value = "/confirm/{id}")
  public CompletableFuture<List<Event>> confirm(@PathVariable String id) {
    return commandGateway.send(ConfirmOrder.builder()
        .id(id)
        .build());
  }

  @GetMapping(value = "/ship/{id}")
  public CompletableFuture<List<Event>> ship(@PathVariable String id) {
    return commandGateway.send(ShipOrder.builder()
        .id(id)
        .trackingNumber(faker.number().digits(12))
        .build());
  }

  @GetMapping(value = "/deliver/{id}")
  public CompletableFuture<List<Event>> deliver(@PathVariable String id) {
    return commandGateway.send(DeliverOrder.builder()
        .id(id)
        .build());
  }

  @GetMapping(value = "/cancel/{id}")
  public CompletableFuture<List<Event>> cancel(@PathVariable String id) {
    return commandGateway.send(CancelOrder.builder()
        .id(id)
        .build());
  }
}
