package com.ora.assessment.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ora.assessment.security.spring.TokenAuthenticationService;

@RestController
@RequestMapping("/auth/logout")
public class LogoutController {

  @Autowired
  private TokenAuthenticationService tokenService;

  @GetMapping
  public ResponseEntity<?> logout(HttpServletRequest request) {
    tokenService.invalidate(request);
    return ResponseEntity.noContent().build();
  }

}
