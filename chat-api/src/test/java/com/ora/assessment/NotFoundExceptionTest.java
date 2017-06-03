package com.ora.assessment;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NotFoundExceptionTest {

  public static final String MESSAGE = "MESSAGE";

  @Test
  public void testConstructor() {
    NotFoundException actual = new NotFoundException(MESSAGE);
    assertEquals(MESSAGE, actual.getMessage());;
  }

}
