package com.ora.assessment;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface Identifiable<T> {

  void setId(T id);

  T getId();

  @JsonIgnore
  default boolean isNew() {
    return null == getId();
  }

}
