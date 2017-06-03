package com.ora.assessment;

import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ora.assessment.resource.ErrorResource;
import com.ora.assessment.validation.ValidationException;

@RestControllerAdvice
public class GlobalExceptionHandlers {

  public static final String VAILATION_FAILED = "Validation Failed";

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

  @ExceptionHandler(UnsupportedOperationException.class)
  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  public ErrorResource unsupported(UnsupportedOperationException ex) {
    return new ErrorResource(ex.getMessage());
  }

  @ExceptionHandler
  // TODO status code
  public ErrorResource failedValidation(ConstraintViolationException ex) {
    ErrorResource resource = new ErrorResource(VAILATION_FAILED);
    ex.getConstraintViolations()
        .forEach(v -> resource.addError(v.getPropertyPath().toString(), v.getMessage()));
    return resource;
  }

  @ExceptionHandler
  // TODO status code
  public ErrorResource failedValidation(ValidationException ex) {
    ErrorResource resource = new ErrorResource(VAILATION_FAILED);
    ex.getErrors().getFieldErrors()
        .forEach(e -> resource.addError(e.getField(), e.getDefaultMessage()));
    return resource;

  }

}
