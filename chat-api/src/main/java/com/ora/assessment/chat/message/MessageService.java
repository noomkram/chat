package com.ora.assessment.chat.message;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ora.assessment.user.UserService;

@Service
@Transactional(readOnly = false)
public class MessageService {

  @Autowired
  private MessageRepository messageRepo;
  @Autowired
  private UserService userService;

  @Transactional(readOnly = false, rollbackFor = Throwable.class)
  public Message save(@Valid Message message) {
    if (!message.isNew()) {
      throw new UnsupportedOperationException("message cannot be updated");
    }

    message.setUser(userService.get(message.getUser().getId()));

    return messageRepo.save(message);
  }

  public Message getLastMessageForChat(long chatId) {
    return messageRepo.findTopByChatIdOrderByCreatedDesc(chatId);
  }

  public Page<Message> getMessagesForChat(long chatId, Pageable page) {
    return messageRepo.findByChatId(chatId, page);
  }

}
