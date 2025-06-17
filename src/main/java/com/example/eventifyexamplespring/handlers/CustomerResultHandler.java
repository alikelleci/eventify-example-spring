package com.example.eventifyexamplespring.handlers;

import com.example.eventifyexamplespring.domain.CustomerCommand.AddCredits;
import com.example.eventifyexamplespring.domain.CustomerCommand.ChangeFirstName;
import com.example.eventifyexamplespring.domain.CustomerCommand.ChangeLastName;
import com.example.eventifyexamplespring.domain.CustomerCommand.CreateCustomer;
import com.example.eventifyexamplespring.domain.CustomerCommand.DeleteCustomer;
import com.example.eventifyexamplespring.domain.CustomerCommand.IssueCredits;
import io.github.alikelleci.eventify.core.messaging.resulthandling.annotations.HandleResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomerResultHandler {

  @HandleResult
  public void handle(CreateCustomer command) {
  }

  @HandleResult
  public void handle(ChangeFirstName command) {
  }

  @HandleResult
  public void handle(ChangeLastName command) {
  }

  @HandleResult
  public void handle(AddCredits event) {
  }

  @HandleResult
  public void handle(IssueCredits event) {
  }

  @HandleResult
  public void handle(DeleteCustomer event) {
  }

}

