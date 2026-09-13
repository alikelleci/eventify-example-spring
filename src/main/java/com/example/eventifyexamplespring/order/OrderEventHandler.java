package com.example.eventifyexamplespring.order;

import com.example.eventifyexamplespring.order.OrderEvent.OrderCancelled;
import com.example.eventifyexamplespring.order.OrderEvent.OrderConfirmed;
import com.example.eventifyexamplespring.order.OrderEvent.OrderDelivered;
import com.example.eventifyexamplespring.order.OrderEvent.OrderPlaced;
import com.example.eventifyexamplespring.order.OrderEvent.OrderShipped;
import io.github.alikelleci.eventify.core.messaging.eventhandling.annotations.HandleEvent;

public class OrderEventHandler {

  @HandleEvent
  public void on(OrderPlaced event) { /* insert into read model */ }

  @HandleEvent
  public void on(OrderConfirmed event) { /* update read model */ }

  @HandleEvent
  public void on(OrderShipped event) { /* send shipping notification */ }

  @HandleEvent
  public void on(OrderDelivered event) { /* update read model */ }

  @HandleEvent
  public void on(OrderCancelled event) { /* remove from read model */ }
}

