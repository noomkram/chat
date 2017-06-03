package com.ora.assessment.chat.message;

import static com.ora.assessment.TestUtils.MESSAGE_ID;
import static com.ora.assessment.TestUtils.USER_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.ora.assessment.user.User;

public class MessageTest {


  private Message message;

  @Before
  public void setup() {
    message = new Message();
  }

  @Test
  public void testBeforeSave() {
    assertNull(message.getCreated());

    message.beforeSave();
    assertNotNull(message.getCreated());
  }

  @Test
  public void testIsNewWhenIsNotNew() {
    message.setId(MESSAGE_ID);
    assertFalse(message.isNew());
  }

  @Test
  public void testIsNewWhenIsNew() {
    assertTrue(message.isNew());
  }

  @Test
  public void testWithUserId() {
    Message message = new Message();
    assertNull(message.getUser());

    message.withUserId(USER_ID);
    assertEquals(USER_ID, message.getUser().getId().longValue());
  }

  @Test
  public void testWithUserIdWhenUserNotNull() {
    User user = new User();
    Message message = new Message();
    message.setUser(user);

    message.withUserId(USER_ID);
    assertEquals(USER_ID, message.getUser().getId().longValue());
  }

}
