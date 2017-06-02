package com.ora.assessment.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class UserResource {

  private long id;
  private String name;
  private String email;

  public UserResource(User user) {
    this.id = user.getId();
    this.name = user.getName();
    this.email = user.getEmail();
  }

}
