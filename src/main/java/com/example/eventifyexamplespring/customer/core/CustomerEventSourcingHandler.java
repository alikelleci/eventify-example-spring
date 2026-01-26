package com.example.eventifyexamplespring.customer.core;

import com.example.eventifyexamplespring.customer.shared.CustomerEvent.CreditsAdded;
import com.example.eventifyexamplespring.customer.shared.CustomerEvent.CreditsIssued;
import com.example.eventifyexamplespring.customer.shared.CustomerEvent.CustomerCreated;
import com.example.eventifyexamplespring.customer.shared.CustomerEvent.CustomerDeleted;
import com.example.eventifyexamplespring.customer.shared.CustomerEvent.FirstNameChanged;
import com.example.eventifyexamplespring.customer.shared.CustomerEvent.LastNameChanged;
import io.github.alikelleci.eventify.core.common.annotations.MessageId;
import io.github.alikelleci.eventify.core.common.annotations.MetadataValue;
import io.github.alikelleci.eventify.core.common.annotations.Timestamp;
import io.github.alikelleci.eventify.core.messaging.Metadata;
import io.github.alikelleci.eventify.core.messaging.eventsourcing.annotations.ApplyEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;

import static io.github.alikelleci.eventify.core.messaging.Metadata.CORRELATION_ID;

@Slf4j
@Component
public class CustomerEventSourcingHandler {

  @ApplyEvent
  public Customer handle(CustomerCreated event,
                         Customer state,
                         Metadata metadata,
                         @Timestamp Instant timestamp,
                         @MessageId String messageId,
                         @MetadataValue(CORRELATION_ID) String correlationId) {
    return Customer.builder()
        .id(event.getId())
        .firstName(event.getFirstName())
        .lastName(event.getLastName())
        .credits(event.getCredits())
        .birthday(event.getBirthday())
        .dateCreated(timestamp)
        .build();
  }

  @ApplyEvent
  public Customer handle(FirstNameChanged event, Customer state) {
    return state.toBuilder()
        .firstName(event.getFirstName())
        .build();
  }

  @ApplyEvent
  public Customer handle(LastNameChanged event, Customer state) {
    return state.toBuilder()
        .lastName(event.getLastName())
        .build();
  }

  @ApplyEvent
  public Customer handle(CreditsAdded event, Customer state) {
    return state.toBuilder()
        .credits(state.getCredits() + event.getAmount())
        .build();
  }

  @ApplyEvent
  public Customer handle(CreditsIssued event, Customer state) {
    return state.toBuilder()
        .credits(state.getCredits() - event.getAmount())
        .build();
  }

  @ApplyEvent
  public Customer handle(CustomerDeleted event, Customer state) {
    return null;
  }
}
