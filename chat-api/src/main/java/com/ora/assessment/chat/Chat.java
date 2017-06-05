package com.ora.assessment.chat;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ora.assessment.Identifiable;
import com.ora.assessment.chat.message.Message;
import com.ora.assessment.user.User;
import com.ora.assessment.validation.ValidationGroups.Creating;
import com.ora.assessment.validation.ValidationGroups.Updating;

import lombok.Data;

@Data
@Entity
@Table(name = "CHATS")
public class Chat implements Identifiable<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "CHAT_ID")
  @NotNull(groups = Updating.class)
  private Long id;
  @NotNull
  @Size(max = 100)
  private String name;
  @NotNull
  @OneToOne
  @JoinColumn(name = "CREATED_BY", updatable = false)
  @JsonIgnore
  private User owner;
  @NotNull(groups = Creating.class)
  @Transient
  @JsonProperty("last_chat_message")
  private Message message;
  @Transient
  private Set<User> users;

  public Chat() {
    this.users = new HashSet<>();
  }

  public void setOwnerId(long ownerId) {
    if (null == owner) {
      owner = new User();
    }
    owner.setId(ownerId);
  }

  public boolean isOwner(long userId) {
    return null != owner && userId == this.owner.getId();
  }


}
