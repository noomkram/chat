package com.ora.assessment.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString
public class DataListResource extends BaseResource {

  // TODO consider builder

  private final Collection<Object> data;

  public DataListResource() {
    super();
    this.data = new ArrayList<>();
  }

  public DataListResource(@NonNull Collection<Object> data) {
    super();
    this.data = data;
  }

  public <T> DataListResource(@NonNull Collection<T> source,
      @NonNull Function<T, ?> resourceFunction) {
    super();
    this.data = source.stream().map(s -> resourceFunction.apply(s)).collect(Collectors.toList());
  }

  public void add(Object data) {
    this.data.add(data);
  }


}
