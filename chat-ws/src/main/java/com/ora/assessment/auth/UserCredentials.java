package com.ora.assessment.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Encapsulates user entered login credentials.
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserCredentials {

  private String email;
  private String password;

}
