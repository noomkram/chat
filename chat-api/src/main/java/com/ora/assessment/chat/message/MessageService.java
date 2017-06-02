package com.ora.assessment.chat.message;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = false)
public class MessageService {

  @Autowired
  private MessageRepository messageRepo;

  @Transactional(readOnly = false, rollbackFor = Throwable.class)
  public Message save(Message message) {
    // TODO validation

    return messageRepo.save(message);
  }

  public Message getLastMessageForChat(long chatId) {
    return messageRepo.findTopByChatIdOrderByCreatedDesc(chatId);
  }

  public List<Message> getMessagesForChat(long chatId) {
    return messageRepo.findByChatId(chatId);
  }

}
