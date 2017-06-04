package com.ora.assessment.user;

import static com.ora.assessment.TestUtils.CHAT_ID;
import static com.ora.assessment.TestUtils.EMAIL;
import static com.ora.assessment.TestUtils.ENCODED_PASSWORD;
import static com.ora.assessment.TestUtils.PASSWORD;
import static com.ora.assessment.TestUtils.USER_ID;
import static com.ora.assessment.TestUtils.populateId;
import static com.ora.assessment.TestUtils.user;
import static java.util.Collections.singleton;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ora.assessment.NotFoundException;
import com.ora.assessment.validation.ValidationException;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

  @InjectMocks
  private UserService service;
  @Mock
  private UserRepository userRepo;
  @Mock
  private PasswordEncoder passwordEncoder;

  @Before
  public void setup() {
    when(userRepo.findOne(USER_ID)).thenReturn(user());
    when(userRepo.findByEmailIgnoreCase(EMAIL)).thenReturn(user());
    when(userRepo.findByChatId(CHAT_ID)).thenReturn(singleton(user()));
    when(userRepo.save(any(User.class))).thenAnswer(populateId);
    when(passwordEncoder.encode(any())).thenReturn(ENCODED_PASSWORD);
    when(passwordEncoder.matches(any(), any())).thenReturn(true);
  }

  @Test
  public void testGet() {
    final User actual = service.get(USER_ID);
    assertNotNull(actual);
  }

  @Test
  public void testGetByEmail() {
    final User actual = service.getByEmail(EMAIL);
    assertNotNull(actual);
  }

  @Test
  public void testCreateWhenPasswordsNotEqual() {
    try {
      service.save(userToCreate(), PASSWORD + PASSWORD);
    } catch (ValidationException expected) {
      assertEquals("passwords do not match",
          expected.getErrors().getFieldError("password").getDefaultMessage());
    }
  }

  @Test
  public void testCreate() {
    final User actual = service.save(userToCreate(), PASSWORD);
    assertNotNull(actual.getId());
    assertEquals(ENCODED_PASSWORD, actual.getPassword());
  }

  @Test(expected = NotFoundException.class)
  public void testUpdateWhenUserNotFound() {
    when(userRepo.findOne(USER_ID)).thenReturn(null);

    service.save(userToUpdate(), PASSWORD);
  }

  @Test
  public void testUpdateWhenPasswordsNotEqual() {
    try {
      service.save(userToUpdate(), PASSWORD + PASSWORD);
    } catch (ValidationException expected) {
      assertEquals("passwords do not match",
          expected.getErrors().getFieldError("password").getDefaultMessage());
    }
  }

  @Test
  public void testUpdateWhenExistingPasswordsNotEqual() {
    when(passwordEncoder.matches(any(), any())).thenReturn(false);

    try {
      service.save(userToUpdate(), PASSWORD);
    } catch (ValidationException expected) {
      assertEquals("passwords do not match",
          expected.getErrors().getFieldError("password").getDefaultMessage());
    }
  }

  @Test
  public void testUpdate() {
    final String UPDATED_EMAIL = "UPDATED_EMAIL";
    final String UPDATED_NAME = "UPDATED_NAME";
    final User expected = userToUpdate();
    expected.setEmail(UPDATED_EMAIL);
    expected.setName(UPDATED_NAME);

    when(userRepo.findOne(USER_ID)).thenReturn(userToUpdate());
    when(userRepo.save(any(User.class))).thenAnswer(returnsFirstArg());

    User actual = service.save(expected, PASSWORD);

    assertEquals(UPDATED_NAME, actual.getName());
    assertEquals(UPDATED_EMAIL, actual.getEmail());
  }

  @Test
  public void testGetUsersInChat() {
    Set<User> actual = service.getUsersInChat(CHAT_ID);
    assertNotNull(actual);
  }

  private User userToCreate() {
    User user = new User();
    user.setPassword(PASSWORD);
    return user;
  }

  private User userToUpdate() {
    User user = userToCreate();
    user.setId(USER_ID);
    return user;
  }

}
