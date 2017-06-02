package com.ora.assessment.chat.message;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class MessageTest {

  public static final long USER_ID = 1L;

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
  public void testSetUserId() {
    Message message = new Message();
    assertNull(message.getUser());

    message.setUserId(USER_ID);
    assertEquals(USER_ID, message.getUser().getId());
  }

}
