package com.ora.assessment.security.spring;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.AuthenticationException;

@RunWith(MockitoJUnitRunner.class)
public class JwtAuthenticationEntryPointTest {

  private JwtAuthenticationEntryPoint entryPoint;
  private AuthenticationException ex;

  @Mock
  private HttpServletRequest request;
  @Mock
  private HttpServletResponse response;

  @Before
  public void setup() {
    entryPoint = new JwtAuthenticationEntryPoint();
  }

  @Test
  public void testCommence() throws IOException {
    entryPoint.commence(request, response, ex);

    verify(response).sendError(SC_UNAUTHORIZED, "Unauthorized");
  }

}
