package com.ora.assessment.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.ora.assessment.Creating;
import com.ora.assessment.NotFoundException;
import com.ora.assessment.Updating;
import com.ora.assessment.chat.message.Message;
import com.ora.assessment.chat.message.MessageService;

@Service
@Transactional(readOnly = true)
public class ChatService {

  @Autowired
  private ChatRepository chatRepo;
  @Autowired
  private MessageService messageService;

  @Transactional(readOnly = false, rollbackFor = Throwable.class)
  public Chat save(Chat chat) {
    if (chat.isNew()) {
      return create(chat);
    }
    return update(chat);
  }

  private Chat create(@Validated(Creating.class) final Chat chat) {
    final Chat savedChat = chatRepo.save(chat);

    final Message message = chat.getMessage();
    message.setChatId(savedChat.getId());

    final Message savedMessage = messageService.save(message);
    savedChat.setMessage(savedMessage);

    return savedChat;
  }

  private Chat update(@Validated(Updating.class) Chat chat) {
    Chat existingChat = chatRepo.findOne(chat.getId());
    if (null == existingChat) {
      throw new NotFoundException("chat not found");
    }
    if (!existingChat.isOwner(chat.getOwner().getId())) {
      throw new IllegalStateException("chat not updatable");
    }

    existingChat.setName(chat.getName());

    final Chat savedChat = chatRepo.save(existingChat);
    savedChat.setMessage(messageService.getLastMessageForChat(chat.getId()));

    return savedChat;
  }

}
