package com.example.eventifyexamplespring.order;

import com.example.eventifyexamplespring.order.OrderCommand.CancelOrder;
import com.example.eventifyexamplespring.order.OrderCommand.ConfirmOrder;
import com.example.eventifyexamplespring.order.OrderCommand.DeliverOrder;
import com.example.eventifyexamplespring.order.OrderCommand.PlaceOrder;
import com.example.eventifyexamplespring.order.OrderCommand.ShipOrder;
import io.github.alikelleci.eventify.core.messaging.resulthandling.annotations.HandleResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
public class OrderResultHandler {

  @HandleResult
  public void handle(PlaceOrder command) {
  }

  @HandleResult
  public void handle(ConfirmOrder command) {
  }

  @HandleResult
  public void handle(ShipOrder command) {
  }

  @HandleResult
  public void handle(DeliverOrder command) {
  }

  @HandleResult
  public void handle(CancelOrder command) {
  }

}

