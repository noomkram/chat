package com.ora.assessment.auth.spring;


import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final TokenAuthenticationService tokenAuthenticationService;

  public JwtAuthenticationFilter(TokenAuthenticationService tokenAuthenticationService) {
    this.tokenAuthenticationService = tokenAuthenticationService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    SecurityContextHolder.getContext()
        .setAuthentication(tokenAuthenticationService.getAuthentication(request));
    filterChain.doFilter(request, response);
  }
}
