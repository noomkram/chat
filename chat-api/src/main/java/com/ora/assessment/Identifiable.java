package com.ora.assessment;

public interface Identifiable<T> {

  void setId(T id);

  T getId();

  default boolean isNew() {
    return null == getId();
  }

}
