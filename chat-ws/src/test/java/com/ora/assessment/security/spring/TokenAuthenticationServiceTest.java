package com.ora.assessment.security.spring;

import static com.ora.assessment.TestUtils.EMAIL;
import static com.ora.assessment.TestUtils.NAME;
import static com.ora.assessment.TestUtils.PASSWORD;
import static com.ora.assessment.TestUtils.USER_ID;
import static com.ora.assessment.TestUtils.token;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.ora.assessment.security.AuthenticatedUser;
import com.ora.assessment.security.Token;
import com.ora.assessment.security.TokenRepository;
import com.ora.assessment.security.spring.TokenAuthenticationService.JwtProperties;
import com.ora.assessment.security.spring.UserDetailsService.AuthenticatedUserDetails;

@RunWith(MockitoJUnitRunner.class)
public class TokenAuthenticationServiceTest {

  private TokenAuthenticationService service;
  private AuthenticatedUserDetails user;
  @Mock
  private TokenRepository tokenRepo;

  @Mock
  private HttpServletRequest request;
  @Mock
  private HttpServletResponse response;
  @Captor
  private ArgumentCaptor<String> jwtCaptor;
  @Captor
  private ArgumentCaptor<Token> tokenCaptor;

  @Before
  public void setup() {
    service = new TokenAuthenticationService(props(), tokenRepo);
    user = new AuthenticatedUserDetails(USER_ID, EMAIL, NAME, PASSWORD);

    doNothing().when(response).addHeader(any(), any());
    when(tokenRepo.findByToken(any())).thenReturn(null);
  }

  @Test
  public void testAddAuthenticationWhenJwtFails() {
    service.addAuthentication(response, null);

    verify(response, never()).addHeader(any(), any());
  }

  @Test
  public void testAddAuthentication() {
    service.addAuthentication(response, user);

    verify(response).addHeader(eq("Authorization"), startsWith("Bearer "));
  }

  @Test
  public void testGetAuthenticationWhenHeaderNotFound() {
    when(request.getHeader("Authorization")).thenReturn(null);

    final AuthenticatedUser actual = service.getAuthentication(request);
    assertNull(actual);
  }

  @Test
  public void testGetAuthenticationWhenSubjectNotFound() {
    setField(user, "username", null);
    final String token = getToken();

    when(request.getHeader("Authorization")).thenReturn(token);

    final AuthenticatedUser actual = service.getAuthentication(request);
    assertNull(actual);
  }

  @Test
  public void testGetAuthenticationWhenTokenExpired() {
    prepareExpiredTokenService();

    final String token = getToken();
    when(request.getHeader("Authorization")).thenReturn(token);

    final AuthenticatedUser actual = service.getAuthentication(request);
    assertNull(actual);
  }

  @Test
  public void testGetAuthenticationWhenTokenInvalidated() {
    final String token = getToken();
    when(request.getHeader("Authorization")).thenReturn(token);
    when(tokenRepo.findByToken(any())).thenReturn(token());

    final AuthenticatedUser actual = service.getAuthentication(request);
    assertNull(actual);
  }

  @Test
  public void testGetAuthentication() {
    final String token = getToken();
    when(request.getHeader("Authorization")).thenReturn(token);

    final AuthenticatedUser actual = service.getAuthentication(request);
    assertEquals(USER_ID, actual.getUserId().longValue());
  }

  @Test
  public void testInvalidateWhenSubjectNotFound() {
    setField(user, "username", null);
    final String token = getToken();
    when(request.getHeader("Authorization")).thenReturn(token);

    service.invalidate(request);

    verify(tokenRepo, never()).save(any(Token.class));
  }

  @Test
  public void testInvalidateWhenHeaderNotFound() {
    when(request.getHeader("Authorization")).thenReturn(null);

    service.invalidate(request);

    verify(tokenRepo, never()).save(any(Token.class));
  }

  @Test
  public void testInvalidateWhenTokenExpired() {
    prepareExpiredTokenService();

    service.invalidate(request);

    verify(tokenRepo, never()).save(any(Token.class));
  }

  @Test
  public void testInvalidate() {
    final String token = getToken();
    when(request.getHeader("Authorization")).thenReturn(token);

    service.invalidate(request);

    verify(tokenRepo).save(tokenCaptor.capture());

    Token actual = tokenCaptor.getValue();
    assertEquals(token, actual.getToken());
  }

  private String getToken() {
    service.addAuthentication(response, user);

    verify(response).addHeader(eq("Authorization"), jwtCaptor.capture());
    return jwtCaptor.getValue().replace("Bearer ", "");
  }

  private JwtProperties props() {
    final JwtProperties props = new JwtProperties();
    props.setExpiration(10000L);
    props.setHeader("Authorization");
    props.setSecret("secret");
    props.setTokenPrefix("Bearer");
    return props;
  }

  private void prepareExpiredTokenService() {
    JwtProperties props = props();
    props.setExpiration(0L);
    service = new TokenAuthenticationService(props, tokenRepo);
  }

}
