package com.ora.assessment.security;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.ora.assessment.security.spring.TokenAuthenticationService;

@RunWith(MockitoJUnitRunner.class)
public class LogoutControllerTest {

  @InjectMocks
  private LogoutController controller;
  @Mock
  private TokenAuthenticationService tokenService;
  @Mock
  private HttpServletRequest request;

  @Test
  public void testLogout() {
    doNothing().when(tokenService).invalidate(request);

    controller.logout(request);

    verify(tokenService).invalidate(request);
  }

}
