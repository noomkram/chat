package com.ora.assessment.resource;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

public class DataListResourceTest {

  public static final String ONE = "1";

  @Test
  public void testConstructor() {
    DataListResource actual = new DataListResource(data());
    assertEquals(data(), actual.getData());
    assertTrue(actual.getMeta().isEmpty());
  }

  @Test
  public void testConstructorWithFunction() {
    DataListResource actual = new DataListResource(data(), Integer::valueOf);
    assertEquals(1, actual.getData().iterator().next());
    assertTrue(actual.getMeta().isEmpty());
  }


  @Test
  public void testConstructorWithPage() {
    DataListResource actual = new DataListResource(page(), Integer::valueOf);
    assertEquals(1, actual.getData().iterator().next());
    assertEquals(page(), actual.getMeta().get(DataListResource.META_KEY));
  }

  private Collection<String> data() {
    return asList(ONE);
  }

  private Page<String> page() {
    return new PageImpl<>((List<String>)data());
  }

}
