package com.ora.assessment.ext.spring;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageRequestWrapperTest {

  public static final int PAGE_NUMBER = 1;
  public static final int PAGE_SIZE = 2;
  public static final Sort SORT = new Sort(Sort.Direction.ASC, "foo");

  private PageRequestWrapper wrapper;
  private Pageable pageable;

  @Before
  public void setup() {
    pageable = new PageRequest(PAGE_NUMBER, PAGE_SIZE, SORT);
  }

  @Test
  public void testGetPageNumberWithoutOffset() {
    wrapper = new PageRequestWrapper(pageable, false);
    assertEquals(PAGE_NUMBER, wrapper.getPageNumber());
  }

  @Test
  public void testGetPageNumberWithOffset() {
    wrapper = new PageRequestWrapper(pageable, true);
    assertEquals(PAGE_NUMBER + 1, wrapper.getPageNumber());
  }

}
