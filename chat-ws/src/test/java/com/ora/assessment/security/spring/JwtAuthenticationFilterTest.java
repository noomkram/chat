package com.ora.assessment.security.spring;

import static com.ora.assessment.TestUtils.EMAIL;
import static com.ora.assessment.TestUtils.NAME;
import static com.ora.assessment.TestUtils.USER_ID;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.ora.assessment.security.AuthenticatedUser;

@RunWith(PowerMockRunner.class)
@PrepareOnlyThisForTest(SecurityContextHolder.class)
public class JwtAuthenticationFilterTest {

  private JwtAuthenticationFilter filter;
  private AuthenticatedUser user;

  @Mock
  private TokenAuthenticationService tokenAuthenticationService;
  @Mock
  private HttpServletRequest request;
  @Mock
  private HttpServletResponse response;
  @Mock
  private FilterChain filterChain;
  @Mock
  private SecurityContext context;

  @Before
  public void setup() {
    filter = new JwtAuthenticationFilter(tokenAuthenticationService);
    user = new AuthenticatedUser(USER_ID, EMAIL, NAME);

    mockStatic(SecurityContextHolder.class);
    when(SecurityContextHolder.getContext()).thenReturn(context);
    when(tokenAuthenticationService.getAuthentication(request)).thenReturn(user);
  }

  @Test
  public void testDoFilterInternal() throws ServletException, IOException {
    filter.doFilterInternal(request, response, filterChain);

    verify(context).setAuthentication(user);
    verify(filterChain).doFilter(request, response);
  }

}
