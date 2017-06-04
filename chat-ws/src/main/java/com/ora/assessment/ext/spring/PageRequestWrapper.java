package com.ora.assessment.ext.spring;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * {@code PageRequest} which modifies {@code #getPageNumber()} based on
 * {@code PageableHandlerMethodArgumentResolver#setOneIndexedParameters(boolean)}. If said property
 * was set to true, {@code #getPageNumber()} shall be incremented by one; necessary for keeping the
 * current page in sync with the submitted page number.
 */
public class PageRequestWrapper extends PageRequest {

  private static final long serialVersionUID = 1281816447893135405L;

  final int offset;

  public PageRequestWrapper(Pageable original, boolean indexOffset) {
    super(original.getPageNumber(), original.getPageSize(), original.getSort());

    this.offset = indexOffset ? 1 : 0;
  }

  @Override
  public int getPageNumber() {
    return super.getPageNumber() + offset;
  }

}
