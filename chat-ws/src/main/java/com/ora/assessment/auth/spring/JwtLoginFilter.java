package com.ora.assessment.auth.spring;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ora.assessment.auth.UserCredentials;
import com.ora.assessment.auth.spring.UserDetailsService.AuthenticatedUserDetails;

public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {

  private final ObjectMapper mapper;
  private final TokenAuthenticationService tokenAuthenticationService;

  public JwtLoginFilter(String url, AuthenticationManager authManager, ObjectMapper mapper,
      TokenAuthenticationService tokenAuthenticationService) {
    super(new AntPathRequestMatcher(url));
    setAuthenticationManager(authManager);
    this.mapper = mapper;
    this.tokenAuthenticationService = tokenAuthenticationService;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
    UserCredentials credentials = mapper.readValue(request.getInputStream(), UserCredentials.class);

    // @formatter:off
    return getAuthenticationManager()
        .authenticate(new UsernamePasswordAuthenticationToken(
            credentials.getEmail(),
            credentials.getPassword(),
            Collections.emptyList())
        );
    // @formatter:on
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authentication) throws IOException, ServletException {

    tokenAuthenticationService.addAuthentication(response,
        (AuthenticatedUserDetails) authentication.getPrincipal());
  }
}
