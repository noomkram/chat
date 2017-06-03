package com.ora.assessment.user;

import static com.ora.assessment.TestUtils.USER_ID;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class UserTest {

  private User user;

  @Before
  public void setup() {
    user = new User();
  }

  @Test
  public void testIsNewWhenIsNotNew() {
    user.setId(USER_ID);
    assertFalse(user.isNew());
  }

  @Test
  public void testIsNewWhenIsNew() {
    assertTrue(user.isNew());
  }

}
