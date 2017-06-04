package com.ora.assessment.security.spring;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.ora.assessment.user.UserService;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserDetailsService
    implements org.springframework.security.core.userdetails.UserDetailsService {

  @Autowired
  private UserService userService;

  @Override
  public UserDetails loadUserByUsername(final String email) {
    com.ora.assessment.user.User user = userService.getByEmail(email);
    if (null == user) {
      log.debug("user not found with email [{}]", email);
      throw new UsernameNotFoundException("not authorized");
    }

    return new AuthenticatedUserDetails(user.getId(), user.getEmail(), user.getName(),
        user.getPassword());
  }

  @Getter
  @Setter
  @ToString
  static class AuthenticatedUserDetails extends org.springframework.security.core.userdetails.User {

    private static final long serialVersionUID = -3323651285077629959L;

    private final Long userId;
    private final String name;

    AuthenticatedUserDetails(Long userId, String email, String name, String password) {
      super(email, password, Collections.emptyList());
      this.userId = userId;
      this.name = name;
    }

  }

}
