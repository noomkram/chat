package com.ora.assessment.chat.message;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.ora.assessment.Identifiable;
import com.ora.assessment.user.User;

import lombok.Data;

@Data
@Entity
@Table(name = "MESSAGES")
public class Message implements Identifiable<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "MESSAGE_ID")
  private Long id;
  @NotNull
  @Size(max = 1000)
  private String message;
  @NotNull
  @Column(name = "CHAT_ID")
  private Long chatId;
  @NotNull
  @OneToOne
  @JoinColumn(name = "USER_ID")
  private User user;
  @Temporal(TemporalType.TIMESTAMP)
  @Column(updatable = false)
  private Date created;

  @PrePersist
  protected void beforeSave() {
    created = new Date();
  }

  public void withUserId(long userId) {
    if (null == user) {
      user = new User();
    }
    user.setId(userId);
  }

}
