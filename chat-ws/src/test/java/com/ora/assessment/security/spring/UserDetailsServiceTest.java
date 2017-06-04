package com.ora.assessment.security.spring;

import static com.ora.assessment.TestUtils.EMAIL;
import static com.ora.assessment.TestUtils.NAME;
import static com.ora.assessment.TestUtils.PASSWORD;
import static com.ora.assessment.TestUtils.USER_ID;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ora.assessment.security.spring.UserDetailsService.AuthenticatedUserDetails;
import com.ora.assessment.user.User;
import com.ora.assessment.user.UserService;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailsServiceTest {

  @InjectMocks
  private UserDetailsService service;
  @Mock
  private UserService userService;

  @Before
  public void setup() {
    when(userService.getByEmail(any())).thenReturn(user());
  }

  @Test(expected = UsernameNotFoundException.class)
  public void testLoadUserByUsernameWhenUserNotFound() {
    when(userService.getByEmail(EMAIL)).thenReturn(null);

    service.loadUserByUsername(EMAIL);
  }

  @Test
  public void testLoadUserByUsername() {
    final AuthenticatedUserDetails actual =
        (AuthenticatedUserDetails) service.loadUserByUsername(EMAIL);
    assertEquals(EMAIL, actual.getUsername());
    assertEquals(PASSWORD, actual.getPassword());
    assertEquals(USER_ID, actual.getUserId().longValue());
    assertEquals(NAME, actual.getName());
  }

  private User user() {
    User user = new User();
    user.setEmail(EMAIL);
    user.setId(USER_ID);
    user.setName(NAME);
    user.setPassword(PASSWORD);
    return user;
  }

}
