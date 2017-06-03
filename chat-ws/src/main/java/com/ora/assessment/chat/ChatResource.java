package com.ora.assessment.chat;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ora.assessment.chat.message.MessageResource;
import com.ora.assessment.user.UserResource;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class ChatResource {

  private long id;
  private String name;
  private List<UserResource> users;
  @Setter
  @JsonProperty("last_chat_message")
  private MessageResource message;

  public ChatResource(Chat chat) {
    this.id = chat.getId();
    this.name = chat.getName();
    this.users = new ArrayList<>();
  }

  public void add(UserResource user) {
    users.add(user);
  }

}
