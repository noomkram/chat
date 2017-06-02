package com.ora.assessment.chat.message;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ora.assessment.chat.Chat;
import com.ora.assessment.user.User;

import lombok.Data;

@Data
@Entity
@Table(name = "MESSAGES")
public class Message {

  // TODO validations/constraints

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "MESSAGE_ID")
  private Integer id;
  private String message;
  @ManyToOne
  @JoinColumn(name = "CHAT_ID")
  private Chat chat;
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

  public void setUserId(long userId) {
    if (null == user) {
      user = new User();
    }
    user.setId(userId);
  }

}
