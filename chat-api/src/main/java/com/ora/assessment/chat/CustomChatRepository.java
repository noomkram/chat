package com.ora.assessment.chat;

import java.util.List;

import org.springframework.data.domain.Pageable;

public interface CustomChatRepository {

  List<Chat> find(Pageable pageable);

}
