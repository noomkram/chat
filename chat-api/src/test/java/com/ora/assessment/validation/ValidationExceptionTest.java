package com.ora.assessment.validation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.validation.Errors;

import com.ora.assessment.validation.ValidationException;

@RunWith(MockitoJUnitRunner.class)
public class ValidationExceptionTest {

  public static final String MESSAGE = "MESSAGE";

  @Mock
  private Errors errors;

  @Test
  public void testConstructorErrors() {
    ValidationException actual = new ValidationException(errors);
    assertEquals(errors, actual.getErrors());
  }

  @Test
  public void testConstructorMessageAndErrors() {
    ValidationException actual = new ValidationException(MESSAGE, errors);
    assertEquals(MESSAGE, actual.getMessage());
    assertEquals(errors, actual.getErrors());
  }

}
