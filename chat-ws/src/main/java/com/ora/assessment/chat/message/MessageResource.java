package com.ora.assessment.chat.message;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ora.assessment.user.UserResource;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MessageResource {

  private long id;
  @JsonProperty("chat_id")
  private long chatId;
  @JsonProperty("user_id")
  private long userId;
  private String message;
  @JsonProperty("created_at")
  private Date created;
  private UserResource user;

  public MessageResource(Message message) {
    this.id = message.getId();
    this.chatId = message.getChatId();
    this.userId = message.getUser().getId();
    this.user = new UserResource(message.getUser());
    this.created = message.getCreated();
    this.message = message.getMessage();
  }

}
