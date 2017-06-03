package com.ora.assessment.chat.message;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

  Message findTopByChatIdOrderByCreatedDesc(long chatId);

  Page<Message> findByChatId(long chatId, Pageable page);

}
