package com.example.eventifyexamplespring.order;

import com.example.eventifyexamplespring.order.OrderCommand.CancelOrder;
import com.example.eventifyexamplespring.order.OrderCommand.ConfirmOrder;
import com.example.eventifyexamplespring.order.OrderCommand.DeliverOrder;
import com.example.eventifyexamplespring.order.OrderCommand.PlaceOrder;
import com.example.eventifyexamplespring.order.OrderCommand.ShipOrder;
import com.github.javafaker.Faker;
import io.github.alikelleci.eventify.core.messaging.commandhandling.gateway.CommandGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping(value = "/customers")
public class OrderController {

  @Autowired
  private CommandGateway commandGateway;
  private Faker faker = new Faker();

  @GetMapping(value = "/place/{id}")
  public CompletableFuture<Object> place(@PathVariable String id) {
    return commandGateway.send(PlaceOrder.builder()
        .id(id)
        .customer(faker.name().fullName())
        .shippingAddress(faker.address().fullAddress())
        .couponCode(faker.commerce().promotionCode(4))
        .build());
  }

  @GetMapping(value = "/confirm/{id}")
  public CompletableFuture<Object> confirm(@PathVariable String id) {
    return commandGateway.send(ConfirmOrder.builder()
        .id(id)
        .build());
  }

  @GetMapping(value = "/ship/{id}")
  public CompletableFuture<Object> ship(@PathVariable String id) {
    return commandGateway.send(ShipOrder.builder()
        .id(id)
        .trackingNumber(faker.number().digits(12))
        .build());
  }

  @GetMapping(value = "/deliver/{id}")
  public CompletableFuture<Object> deliver(@PathVariable String id) {
    return commandGateway.send(DeliverOrder.builder()
        .id(id)
        .build());
  }

  @GetMapping(value = "/cancel/{id}")
  public CompletableFuture<Object> cancel(@PathVariable String id) {
    return commandGateway.send(CancelOrder.builder()
        .id(id)
        .build());
  }
}
