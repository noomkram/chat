package com.ora.assessment.resource;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DataResourceTest {

  @Test
  public void testConstructor() {
    final String expected = "data";

    final DataResource<String> actual = new DataResource<>(expected);
    assertEquals(expected, actual.getData());
  }

  @Test
  public void testConstructorWithFunction() {
    final String expected = "1";

    final DataResource<Integer> actual = new DataResource<>(expected, Integer::valueOf);
    assertEquals(1, actual.getData().intValue());
  }

}
