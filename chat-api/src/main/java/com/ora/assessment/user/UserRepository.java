package com.ora.assessment.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  User findByEmailIgnoreCase(String email);

}
