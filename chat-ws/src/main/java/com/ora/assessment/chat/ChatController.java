package com.ora.assessment.chat;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ora.assessment.GlobalExceptionHandlers;
import com.ora.assessment.auth.AuthenticatedUser;
import com.ora.assessment.chat.message.Message;
import com.ora.assessment.chat.message.MessageResource;
import com.ora.assessment.resource.DataResource;
import com.ora.assessment.resource.ErrorResource;
import com.ora.assessment.user.UserResource;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/chats", consumes = APPLICATION_JSON_VALUE)
@Slf4j
public class ChatController {

  @Autowired
  private ChatService chatService;

  @PostMapping
  public ResponseEntity<DataResource<ChatResource>> create(AuthenticatedUser user,
      @RequestBody CreateChat createChat) {
    log.trace("POST /chats : {}", createChat);

    Message message = new Message();
    message.withUserId(user.getUserId());
    message.setMessage(createChat.getMessage());

    Chat chat = new Chat();
    chat.setMessage(message);
    chat.setName(createChat.getName());
    chat.setOwnerId(user.getUserId());

    return ResponseEntity.ok(asDataResource(chatService.save(chat)));
  }

  @PatchMapping("/{chatId}")
  public ResponseEntity<DataResource<ChatResource>> update(AuthenticatedUser user, @PathVariable Long chatId,
      @RequestBody UpdateChat updatedChat) {
    Chat chat = new Chat();
    chat.setId(chatId);
    chat.setName(updatedChat.getName());
    chat.setOwnerId(user.getUserId());

    return ResponseEntity.status(CREATED).body(asDataResource(chatService.save(chat)));
  }

  @ExceptionHandler
  public ErrorResource duplicateChatName(DataIntegrityViolationException ex) {
    return new ErrorResource(GlobalExceptionHandlers.VAILATION_FAILED).addError("name", "chat with name already exists");
  }

  private DataResource<ChatResource> asDataResource(Chat chat) {
    ChatResource chatResource = new ChatResource(chat);
    chatResource.setMessage(new MessageResource(chat.getMessage()));
    chatResource.add(new UserResource(chat.getOwner()));
    return new DataResource<>(chatResource);
  }

}
