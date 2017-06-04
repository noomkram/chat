package com.ora.assessment.security.spring;

import static com.ora.assessment.TestUtils.EMAIL;
import static com.ora.assessment.TestUtils.NAME;
import static com.ora.assessment.TestUtils.PASSWORD;
import static com.ora.assessment.TestUtils.USER_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ora.assessment.security.UserCredentials;
import com.ora.assessment.security.spring.UserDetailsService.AuthenticatedUserDetails;

@RunWith(MockitoJUnitRunner.class)
public class JwtLoginFilterTest {

  public static final String URL = "URL";

  private JwtLoginFilter filter;
  @Mock
  private ObjectMapper mapper;
  @Mock
  private TokenAuthenticationService tokenAuthenticationService;
  @Mock
  private AuthenticationManager authManager;
  @Mock
  private Authentication auth;
  @Mock
  private FilterChain chain;
  @Mock
  private HttpServletRequest request;
  @Mock
  private HttpServletResponse response;
  @Captor
  private ArgumentCaptor<UsernamePasswordAuthenticationToken> tokenCaptor;
  private AuthenticatedUserDetails user;

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Before
  public void setup() throws JsonParseException, JsonMappingException, IOException {
    user = new AuthenticatedUserDetails(USER_ID, EMAIL, NAME, PASSWORD);

    filter = new JwtLoginFilter(URL, authManager, mapper, tokenAuthenticationService);

    when(request.getInputStream()).thenReturn(mock(ServletInputStream.class));
    when(mapper.readValue(any(InputStream.class), (Class) any())).thenReturn(userCredentials());
    when(authManager.authenticate(any())).thenReturn(auth);
    when(auth.getPrincipal()).thenReturn(user);
  }

  @Test
  public void testAttemptAuthentication()
      throws AuthenticationException, IOException, ServletException {
    Authentication actual = filter.attemptAuthentication(request, response);
    assertNotNull(actual);

    verify(authManager).authenticate(tokenCaptor.capture());
    UsernamePasswordAuthenticationToken actualToken = tokenCaptor.getValue();
    assertEquals(EMAIL, actualToken.getName());
    assertEquals(PASSWORD, actualToken.getCredentials());
  }

  @Test
  public void testSuccessfulAuthentication() throws IOException, ServletException {
    filter.successfulAuthentication(request, response, chain, auth);

    verify(tokenAuthenticationService).addAuthentication(response, user);
  }

  private UserCredentials userCredentials() {
    UserCredentials userCredentials = new UserCredentials();
    userCredentials.setEmail(EMAIL);
    userCredentials.setPassword(PASSWORD);
    return userCredentials;
  }

}
