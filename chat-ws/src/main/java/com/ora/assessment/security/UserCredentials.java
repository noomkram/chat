package com.ora.assessment.security;

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
