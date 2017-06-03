package com.ora.assessment.security.spring;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

  private static final long serialVersionUID = 1899770645129529461L;

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException {

    // TODO need formatted response, currently returning
    // {
    // "timestamp": "2017-06-02T04:12:17UTC",
    // "status": 401,
    // "error": "Unauthorized",
    // "message": "Unauthorized",
    // "path": "/users/current"
    // }

    response.sendError(SC_UNAUTHORIZED, "Unauthorized");
  }
}
