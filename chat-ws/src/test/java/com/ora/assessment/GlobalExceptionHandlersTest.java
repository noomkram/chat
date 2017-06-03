package com.ora.assessment;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import com.ora.assessment.resource.ErrorResource;
import com.ora.assessment.validation.ValidationException;

@RunWith(MockitoJUnitRunner.class)
public class GlobalExceptionHandlersTest {

  public static final String PATH = "PATH";
  public static final String MESSAGE = "MESSAGE";

  private GlobalExceptionHandlers handlers;

  @Before
  public void setup() {
    handlers = new GlobalExceptionHandlers();
  }

  @Test
  public void testNotFound() {
    NotFoundException ex = new NotFoundException(MESSAGE);

    ErrorResource actual = handlers.notFound(ex);
    assertEquals(MESSAGE, actual.getMessage());
    assertTrue(actual.getErrors().isEmpty());
    assertTrue(actual.getMeta().isEmpty());
  }

  @Test
  public void testIllegalState() {
    IllegalStateException ex = new IllegalStateException(MESSAGE);

    ErrorResource actual = handlers.illegalState(ex);
    assertEquals(MESSAGE, actual.getMessage());
    assertTrue(actual.getErrors().isEmpty());
    assertTrue(actual.getMeta().isEmpty());
  }

  @Test
  public void testUnsupported() {
    UnsupportedOperationException ex = new UnsupportedOperationException(MESSAGE);

    ErrorResource actual = handlers.unsupported(ex);
    assertEquals(MESSAGE, actual.getMessage());
    assertTrue(actual.getErrors().isEmpty());
    assertTrue(actual.getMeta().isEmpty());
  }

  @Test
  public void testFailedValidationWhenConstraintViolation() {
    ConstraintViolationException ex = new ConstraintViolationException(violations());

    ErrorResource actual = handlers.failedValidation(ex);
    assertEquals(GlobalExceptionHandlers.VAILATION_FAILED, actual.getMessage());
    assertEquals(MESSAGE, actual.getErrors().get(PATH).get(0));
  }


  @Test
  public void testFailedValidationWhenValidationException() {
    ValidationException ex = new ValidationException(errors());

    ErrorResource actual = handlers.failedValidation(ex);
    assertEquals(GlobalExceptionHandlers.VAILATION_FAILED, actual.getMessage());
    assertEquals(MESSAGE, actual.getErrors().get(PATH).get(0));
  }

  private Set<ConstraintViolation<?>> violations() {
    Path path = mock(Path.class);
    when(path.toString()).thenReturn(PATH);

    ConstraintViolation<?> v = mock(ConstraintViolation.class);
    when(v.getPropertyPath()).thenReturn(path);
    when(v.getMessage()).thenReturn(MESSAGE);

    Set<ConstraintViolation<?>> set = new HashSet<>();
    set.add(v);
    return set;
  }

  private Errors errors() {
    FieldError fieldError = mock(FieldError.class);
    when(fieldError.getField()).thenReturn(PATH);
    when(fieldError.getDefaultMessage()).thenReturn(MESSAGE);

    Errors errors = mock(Errors.class);
    when(errors.getFieldErrors()).thenReturn(asList(fieldError));

    return errors;
  }

}
