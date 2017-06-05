package com.ora.assessment.security;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, UUID> {

  Token findByToken(String token);

}
