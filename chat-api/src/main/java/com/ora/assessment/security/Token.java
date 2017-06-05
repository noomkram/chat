package com.ora.assessment.security;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TOKEN_BLACKLIST")
@Data
@NoArgsConstructor
public class Token {

  @Id
  @GenericGenerator(name = "uuid-gen", strategy = "uuid2")
  @GeneratedValue(generator = "uuid-gen")
  @Column(name = "TOKEN_ID")
  private UUID id;
  @Column
  private String token;
  @Temporal(TemporalType.TIMESTAMP)
  @Column(updatable = false)
  private Date expires;

}
