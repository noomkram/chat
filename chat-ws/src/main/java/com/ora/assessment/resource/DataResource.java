package com.ora.assessment.resource;

import java.util.function.Function;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class DataResource extends BaseResource {

  private final Object data;

  public DataResource(Object data) {
    super();
    this.data = data;
  }

  public <T> DataResource(T data, Function<T, ?> resourceFunction) {
    super();
    this.data = resourceFunction.apply(data);
  }

}
