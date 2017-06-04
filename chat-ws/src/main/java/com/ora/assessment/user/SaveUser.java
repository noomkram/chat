package com.ora.assessment.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SaveUser {

  private Long id;
  private String name;
  private String email;
  private String password;
  @JsonProperty("password_confirmation")
  private String passwordConfirmation;

}
