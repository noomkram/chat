package com.ora.assessment.resource;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.ToString;

@ToString
public class ErrorResource extends BaseResource {

  @JsonProperty
  private final String message;
  @JsonProperty
  private final MultiValueMap<String, String> errors;

  public ErrorResource(String message) {
    super();
    this.message = message;
    this.errors = new LinkedMultiValueMap<>();
  }

  public ErrorResource addError(String key, String message) {
    this.errors.add(key, message);
    return this;
  }

}
