package com.ora.assessment.auth;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;
import lombok.ToString;

/**
 * Represents an authenticated user.
 */
@ToString
public class AuthenticatedUser implements Authentication {

  private static final long serialVersionUID = -4818820118290715241L;

  @Getter
  private Long userId;
  @Getter
  private final String email;
  private final String name;
  private boolean authenticated = true;

  public AuthenticatedUser(Long userId, String email, String name) {
    this.userId = userId;
    this.email = email;
    this.name = name;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return null;
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Object getDetails() {
    return null;
  }

  @Override
  public Object getPrincipal() {
    return null;
  }

  @Override
  public boolean isAuthenticated() {
    return this.authenticated;
  }

  @Override
  public void setAuthenticated(boolean b) throws IllegalArgumentException {
    this.authenticated = b;
  }

  @Override
  public String getName() {
    return this.name;
  }

}
