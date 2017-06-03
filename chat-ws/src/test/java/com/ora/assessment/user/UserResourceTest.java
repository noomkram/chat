package com.ora.assessment.user;

import static com.ora.assessment.TestUtils.EMAIL;
import static com.ora.assessment.TestUtils.NAME;
import static com.ora.assessment.TestUtils.USER_ID;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class UserResourceTest {

  private User user;

  @Before
  public void setup() {
    user = new User();
    user.setEmail(EMAIL);
    user.setId(USER_ID);
    user.setName(NAME);
  }

  @Test
  public void testConstructor() {
    final UserResource actual = new UserResource(user);
    assertEquals(USER_ID, actual.getId());
    assertEquals(EMAIL, actual.getEmail());
    assertEquals(NAME, actual.getName());
  }

}
