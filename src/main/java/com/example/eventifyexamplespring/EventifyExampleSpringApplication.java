package com.example.eventifyexamplespring;

import com.example.eventifyexamplespring.order.OrderCommand;
import com.github.javafaker.Faker;
import io.github.alikelleci.eventify.core.command.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EventifyExampleSpringApplication implements CommandLineRunner {

  @Autowired
  private CommandGateway commandGateway;
  private Faker faker = new Faker();

  public static void main(String[] args) {
    SpringApplication.run(EventifyExampleSpringApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {

//    for (int i = 0; i <= 1000; i++) {
//      commandGateway.send(OrderCommand.PlaceOrder.builder()
//          .id("order-10000")
//          .customer(faker.name().fullName())
//          .shippingAddress(faker.address().fullAddress())
//          .couponCode(faker.commerce().promotionCode(4))
//          .build());
//    }

//    for (int i = 0; i <= 10; i++) {
//     createOrder("2020-" + i);
//    }
  }

  void createOrder(String id) throws InterruptedException {
    commandGateway.send(OrderCommand.PlaceOrder.builder()
        .id(id)
        .customer(faker.name().fullName())
        .shippingAddress(faker.address().fullAddress())
        .couponCode(faker.commerce().promotionCode(4))
        .build());

    Thread.sleep(1000);
    commandGateway.send(OrderCommand.ConfirmOrder.builder()
        .id(id)
        .build());

    Thread.sleep(1000);
    commandGateway.send(OrderCommand.ShipOrder.builder()
        .id(id)
        .trackingNumber(faker.number().digits(12))
        .build());

    Thread.sleep(1000);
    commandGateway.send(OrderCommand.DeliverOrder.builder()
        .id(id)
        .build());

    Thread.sleep(1000);
    commandGateway.send(OrderCommand.CancelOrder.builder()
        .id(id)
        .build());
  }
}
