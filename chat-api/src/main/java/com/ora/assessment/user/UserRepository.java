package com.ora.assessment.user;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

  User findByEmailIgnoreCase(String email);

  @Query(nativeQuery = true,
      value= "select distinct u.* from messages m join chat_users u on m.user_id = u.user_id where m.chat_id = ?")
  Set<User> findByChatId(long chatId);

}
