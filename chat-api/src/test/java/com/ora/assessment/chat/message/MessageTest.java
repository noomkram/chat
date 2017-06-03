package com.ora.assessment.chat.message;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class MessageTest {

  public static final long USER_ID = 1L;
  public static final long CHAT_ID = 2L;

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
  public void testWithUserId() {
    Message message = new Message();
    assertNull(message.getUser());

    message.withUserId(USER_ID);
    assertEquals(USER_ID, message.getUser().getId().longValue());
  }

}
