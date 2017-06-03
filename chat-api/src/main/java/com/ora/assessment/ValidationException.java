package com.ora.assessment;

import org.springframework.validation.Errors;

import lombok.Getter;

public class ValidationException extends RuntimeException {

  private static final long serialVersionUID = -3102557157116889009L;

  @Getter
  private final Errors errors;

  public ValidationException(Errors errors) {
    this("", errors);
  }

  public ValidationException(String message, Errors errors) {
    super(message);
    this.errors = errors;
  }

}
