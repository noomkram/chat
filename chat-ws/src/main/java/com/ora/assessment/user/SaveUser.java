package com.ora.assessment.user;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SaveUser {

  private Long id;
  private String name;
  private String email;
  private String password;
  private String confirmPassword;

}
