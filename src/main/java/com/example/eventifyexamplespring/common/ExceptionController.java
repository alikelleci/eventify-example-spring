package com.example.eventifyexamplespring.common;

import io.github.alikelleci.eventify.core.messaging.commandhandling.exceptions.CommandExecutionException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ExceptionController {


  @ExceptionHandler(value = CommandExecutionException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public Map<String, String> exception(HttpServletRequest request, CommandExecutionException e) {
    log.warn(e.getMessage());

    String code = StringUtils.defaultString(StringUtils.substringBetween(e.getMessage(), "[", "]"), "").strip();
    String message = StringUtils.split(e.getMessage(), ":", 2)[1].replace(code, "").replace("[]", "").strip();

    return Map.of(
        "message", message,
        "code", code
    );
  }

}
