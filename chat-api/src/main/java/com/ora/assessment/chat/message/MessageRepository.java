package com.ora.assessment.chat.message;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

  Message findTopByChatIdOrderByCreatedDesc(long chatId);

  List<Message> findByChatId(long chatId);

}
