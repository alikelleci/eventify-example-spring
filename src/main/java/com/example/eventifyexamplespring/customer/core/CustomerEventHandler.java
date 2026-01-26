package com.example.eventifyexamplespring.customer.core;

import com.example.eventifyexamplespring.customer.shared.CustomerEvent.CustomerCreated;
import com.example.eventifyexamplespring.customer.shared.CustomerEvent.CustomerDeleted;
import com.example.eventifyexamplespring.customer.shared.CustomerEvent.FirstNameChanged;
import com.example.eventifyexamplespring.customer.shared.CustomerEvent.LastNameChanged;
import io.github.alikelleci.eventify.core.messaging.eventhandling.annotations.HandleEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.example.eventifyexamplespring.customer.shared.CustomerEvent.CreditsAdded;
import static com.example.eventifyexamplespring.customer.shared.CustomerEvent.CreditsIssued;

@Slf4j
@Component
public class CustomerEventHandler {

  @HandleEvent
  public void handle(CustomerCreated event) {
  }

  @HandleEvent
  public void handle(FirstNameChanged event) {
  }

  @HandleEvent
  public void handle(LastNameChanged event) {
  }

  @HandleEvent
  public void handle(CreditsAdded event) {
  }

  @HandleEvent
  public void handle(CreditsIssued event) {
  }

  @HandleEvent
  public void handle(CustomerDeleted event) {
  }

}

