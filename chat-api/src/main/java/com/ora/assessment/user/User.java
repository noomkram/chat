package com.ora.assessment.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ora.assessment.Identifiable;
import com.ora.assessment.validation.ValidationGroups.Updating;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "CHAT_USERS")
public class User implements Identifiable<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "USER_ID")
  @NotNull(groups = Updating.class)
  private Long id;
  @NotNull
  @Size(max = 100)
  private String name;
  @NotNull
  @Size(max = 100)
  private String email;
  @NotNull
  @Size(max = 100)
  @JsonIgnore
  private String password;

}
