package com.example.eventifyexamplespring.order;

import com.example.eventifyexamplespring.order.OrderCommand.CancelOrder;
import com.example.eventifyexamplespring.order.OrderCommand.ConfirmOrder;
import com.example.eventifyexamplespring.order.OrderCommand.DeliverOrder;
import com.example.eventifyexamplespring.order.OrderCommand.PlaceOrder;
import com.example.eventifyexamplespring.order.OrderCommand.ShipOrder;
import com.example.eventifyexamplespring.order.OrderEvent.OrderCancelled;
import com.example.eventifyexamplespring.order.OrderEvent.OrderConfirmed;
import com.example.eventifyexamplespring.order.OrderEvent.OrderDelivered;
import com.example.eventifyexamplespring.order.OrderEvent.OrderPlaced;
import com.example.eventifyexamplespring.order.OrderEvent.OrderShipped;
import io.github.alikelleci.eventify.core.common.annotations.MessageId;
import io.github.alikelleci.eventify.core.common.annotations.MetadataValue;
import io.github.alikelleci.eventify.core.common.annotations.Timestamp;
import io.github.alikelleci.eventify.core.messaging.Metadata;
import io.github.alikelleci.eventify.core.messaging.commandhandling.annotations.HandleCommand;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

import java.time.Instant;

import static io.github.alikelleci.eventify.core.messaging.Metadata.CORRELATION_ID;

@Component
public class OrderCommandHandler {

  @HandleCommand
  public OrderEvent handle(PlaceOrder command, Order state,
                           Metadata metadata,
                           @Timestamp Instant timestamp,
                           @MessageId String messageId,
                           @MetadataValue(CORRELATION_ID) String correlationId) {
    if (state != null) throw new ValidationException("Order already exists.");
    return OrderPlaced.builder()
        .id(command.getId())
        .customer(command.getCustomer())
        .shippingAddress(command.getShippingAddress())
        .couponCode(command.getCouponCode())
        .build();
  }

  @HandleCommand
  public OrderEvent handle(ConfirmOrder command, Order state) {
    if (state == null) throw new ValidationException("Order does not exist.");
    if (!"PLACED".equals(state.getStatus())) throw new ValidationException("Order cannot be confirmed.");
    return OrderConfirmed.builder()
        .id(command.getId())
        .build();
  }

  @HandleCommand
  public OrderEvent handle(ShipOrder command, Order state) {
    if (state == null) throw new ValidationException("Order does not exist.");
    if (!"CONFIRMED".equals(state.getStatus())) throw new ValidationException("Order cannot be shipped.");
    return OrderShipped.builder()
        .id(command.getId())
        .trackingNumber(command.getTrackingNumber())
        .build();
  }

  @HandleCommand
  public OrderEvent handle(DeliverOrder command, Order state) {
    if (state == null) throw new ValidationException("Order does not exist.");
    if (!"SHIPPED".equals(state.getStatus())) throw new ValidationException("Order cannot be delivered.");
    return OrderDelivered.builder()
        .id(command.getId())
        .build();
  }

  @HandleCommand
  public OrderEvent handle(CancelOrder command, Order state) {
    if (state == null) throw new ValidationException("Order does not exist.");
    if ("SHIPPED".equals(state.getStatus()) || "DELIVERED".equals(state.getStatus()))
      throw new ValidationException("Order cannot be cancelled.");
    return OrderCancelled.builder()
        .id(command.getId())
        .reason(command.getReason())
        .build();
  }
}