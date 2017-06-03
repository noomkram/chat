package com.ora.assessment.resource;

import java.util.function.Function;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class DataResource<T> extends BaseResource {

  private final T data;

  public DataResource(T data) {
    super();
    this.data = data;
  }

  public <I> DataResource(I data, Function<I, T> resourceFunction) {
    super();
    this.data = resourceFunction.apply(data);
  }

}
