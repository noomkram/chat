package com.ora.assessment.user;

import static com.ora.assessment.TestUtils.EMAIL;
import static com.ora.assessment.TestUtils.NAME;
import static com.ora.assessment.TestUtils.PASSWORD;
import static com.ora.assessment.TestUtils.USER_ID;
import static com.ora.assessment.TestUtils.authenticatedUser;
import static com.ora.assessment.TestUtils.populateId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import com.ora.assessment.resource.DataResource;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

  @InjectMocks
  private UserController controller;
  @Mock
  private UserService userService;
  @Captor
  private ArgumentCaptor<User> userCaptor;

  @Before
  public void setup() {
    when(userService.get(anyLong())).thenReturn(existingUser());
  }

  @Test
  public void testCreate() {
    when(userService.save(any(), eq(PASSWORD))).then(populateId);

    final ResponseEntity<DataResource<UserResource>> actualResponse = controller.create(saveUser());
    assertEquals(CREATED, actualResponse.getStatusCode());

    verify(userService).save(userCaptor.capture(), eq(PASSWORD));
    final User actualUser = userCaptor.getValue();
    assertNotNull(actualUser.getId());
    assertEquals(EMAIL, actualUser.getEmail());
    assertEquals(NAME, actualUser.getName());
    assertEquals(PASSWORD, actualUser.getPassword());

    final UserResource actualUserResource = actualResponse.getBody().getData();
    assertNotNull(actualUserResource);
  }

  @Test
  public void testUpdate() {
    when(userService.save(any(), eq(PASSWORD))).then(returnsFirstArg());

    final ResponseEntity<DataResource<UserResource>> actualResponse =
        controller.update(authenticatedUser(), saveUser());
    assertEquals(OK, actualResponse.getStatusCode());

    verify(userService).save(userCaptor.capture(), eq(PASSWORD));
    final User actualUser = userCaptor.getValue();
    assertNotNull(actualUser.getId());
    assertEquals(EMAIL, actualUser.getEmail());
    assertEquals(NAME, actualUser.getName());
    assertEquals(PASSWORD, actualUser.getPassword());

    final UserResource actualUserResource = actualResponse.getBody().getData();
    assertNotNull(actualUserResource);
  }

  @Test
  public void testGet() {
    final ResponseEntity<DataResource<UserResource>> actualResponse =
        controller.get(authenticatedUser());
    assertEquals(OK, actualResponse.getStatusCode());

    final UserResource actualUserResource = actualResponse.getBody().getData();
    assertNotNull(actualUserResource);

    verify(userService).get(USER_ID);
  }

  private User existingUser() {
    final User u = new User();
    u.setId(USER_ID);
    u.setEmail(EMAIL);
    u.setName(NAME);
    return u;
  }

  private SaveUser saveUser() {
    final SaveUser saveUser = new SaveUser();
    saveUser.setConfirmPassword(PASSWORD);
    saveUser.setEmail(EMAIL);
    saveUser.setName(NAME);
    saveUser.setPassword(PASSWORD);
    return saveUser;
  }

}
