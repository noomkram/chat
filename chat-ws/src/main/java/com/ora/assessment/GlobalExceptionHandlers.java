package com.ora.assessment;

import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ora.assessment.resource.ErrorResource;

@RestControllerAdvice
public class GlobalExceptionHandlers {

  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(NOT_FOUND)
  public ErrorResource notFound(NotFoundException ex) {
    return new ErrorResource(ex.getMessage());
  }

  @ExceptionHandler(IllegalStateException.class)
  @ResponseStatus(METHOD_NOT_ALLOWED)
  public ErrorResource illegalState(IllegalStateException ex) {
    return new ErrorResource(ex.getMessage());
  }

}
