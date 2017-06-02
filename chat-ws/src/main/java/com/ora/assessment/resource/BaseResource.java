package com.ora.assessment.resource;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString
public abstract class BaseResource implements Resource {

  private final Map<String, Object> meta;

  public BaseResource() {
    super();
    this.meta = new HashMap<>();
  }

  public BaseResource(@NonNull String key, Object value) {
    this();
    addMeta(key, value);
  }

  public void addMeta(String key, Object value) {
    this.meta.put(key, value);
  }

}
