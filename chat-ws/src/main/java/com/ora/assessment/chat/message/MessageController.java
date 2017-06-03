package com.ora.assessment.chat.message;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ora.assessment.resource.DataListResource;
import com.ora.assessment.resource.DataResource;
import com.ora.assessment.resource.Resource;
import com.ora.assessment.security.AuthenticatedUser;

@RestController
@RequestMapping(value = "/chats/{chatId}/chat_messages", consumes = APPLICATION_JSON_VALUE)
public class MessageController {

  @Autowired
  private MessageService messageService;

  @PostMapping
  public ResponseEntity<DataResource<MessageResource>> create(AuthenticatedUser user, @PathVariable Long chatId,
      @RequestBody SaveMessage message) {

    Message newMessage = new Message();
    newMessage.setChatId(chatId);
    newMessage.setMessage(message.getMessage());
    newMessage.withUserId(user.getUserId());

    return ResponseEntity.status(CREATED)
        .body(new DataResource<MessageResource>(messageService.save(newMessage), MessageResource::new));
  }

  // TODO remove AuthenticatedUser, not needed
  @GetMapping
  public ResponseEntity<Resource> getMessages(AuthenticatedUser user, @PathVariable Long chatId,
      Pageable page) {
    Page<Message> p = messageService.getMessagesForChat(chatId, page);

    // @formatter:off
    return ResponseEntity.ok(
        new DataListResource(
            p.getContent(),
            MessageResource::new
        )
        .addMeta("pagination", p)
    );
    // @formatter:on
  }

}
