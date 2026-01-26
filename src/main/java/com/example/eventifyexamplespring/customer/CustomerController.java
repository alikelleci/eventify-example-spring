package com.example.eventifyexamplespring.customer;

import com.example.eventifyexamplespring.customer.shared.CustomerCommand.AddCredits;
import com.example.eventifyexamplespring.customer.shared.CustomerCommand.DeleteCustomer;
import com.example.eventifyexamplespring.customer.shared.CustomerCommand.IssueCredits;
import com.github.javafaker.Faker;
import io.github.alikelleci.eventify.core.messaging.commandhandling.gateway.CommandGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

import static com.example.eventifyexamplespring.customer.shared.CustomerCommand.CreateCustomer;

@Slf4j
@RestController
@RequestMapping(value = "/customers")
public class CustomerController {

  @Autowired
  private CommandGateway commandGateway;
  private Faker faker = new Faker();

  @GetMapping(value = "/create/{id}")
  public CompletableFuture<Object> create(@PathVariable String id) {
    return commandGateway.send(CreateCustomer.builder()
        .id(id)
        .firstName(faker.name().firstName())
        .lastName(faker.name().lastName())
        .birthday(Instant.now())
        .credits(100)
        .build());
  }

  @GetMapping(value = "/addCredits/{id}")
  public CompletableFuture<Object> addCredits(@PathVariable String id) {
    return commandGateway.send(AddCredits.builder()
        .id(id)
        .amount(25)
        .build());
  }

  @GetMapping(value = "/issueCredits/{id}")
  public CompletableFuture<Object> issueCredits(@PathVariable String id) {
    return commandGateway.send(IssueCredits.builder()
        .id(id)
        .amount(25)
        .build());
  }

  @GetMapping(value = "/delete/{id}")
  public CompletableFuture<Object> delete(@PathVariable String id) {
    return commandGateway.send(DeleteCustomer.builder()
        .id(id)
        .build());
  }
}
