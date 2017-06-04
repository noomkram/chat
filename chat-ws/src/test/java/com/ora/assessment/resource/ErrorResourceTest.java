package com.ora.assessment.resource;

import static com.ora.assessment.TestUtils.MESSAGE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ErrorResourceTest {

  public static final String ERROR_KEY = "ERROR_KEY";
  public static final String ERROR_MESSAGE = "ERROR_MESSAGE";

  @Test
  public void testConstructor() {
    ErrorResource actual = new ErrorResource(MESSAGE);
    assertEquals(MESSAGE, actual.getMessage());
    assertTrue(actual.getErrors().isEmpty());
  }

  @Test
  public void testAddError() {
    ErrorResource actual = new ErrorResource(MESSAGE);
    actual.addError(ERROR_KEY, ERROR_MESSAGE);

    assertEquals(MESSAGE, actual.getMessage());
    assertEquals(ERROR_MESSAGE, actual.getErrors().get(ERROR_KEY).get(0));
  }

}
