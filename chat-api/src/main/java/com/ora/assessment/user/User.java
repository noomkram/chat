package com.ora.assessment.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@Entity
@Table(name = "CHAT_USERS")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "USER_ID")
  private long id;
  @NotNull
  @Size(max = 100)
  @NonNull
  private String name;
  @NotNull
  @Size(max = 100)
  @NonNull
  private String email;
  @NotNull
  @Size(max = 100)
  @NonNull
  private String password;

}
