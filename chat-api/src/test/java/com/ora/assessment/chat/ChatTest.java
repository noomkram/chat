package com.ora.assessment.chat;

import static com.ora.assessment.TestUtils.CHAT_ID;
import static com.ora.assessment.TestUtils.OWNER_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.ora.assessment.user.User;

public class ChatTest {

  private Chat chat;
  private User user;

  @Before
  public void setup() {
    user = new User();
    user.setId(OWNER_ID);

    chat = new Chat();
    chat.setOwner(user);
  }

  @Test
  public void testIsNewWhenIsNotNew() {
    chat.setId(CHAT_ID);
    assertFalse(chat.isNew());
  }

  @Test
  public void testIsNewWhenIsNew() {
    assertTrue(chat.isNew());
  }

  @Test
  public void testIsOwnerWhenIsNotOwner() {
    assertFalse(chat.isOwner(OWNER_ID + 1));

    chat.setOwner(null);
    assertFalse(chat.isOwner(OWNER_ID + 1));
  }

  @Test
  public void testIsOwnerWhenIsOwner() {
    assertTrue(chat.isOwner(OWNER_ID));
  }

  @Test
  public void testSetOwnerId() {
    Chat chat = new Chat();
    assertNull(chat.getOwner());

    chat.setOwnerId(OWNER_ID);
    assertEquals(OWNER_ID, chat.getOwner().getId().longValue());
  }

  @Test
  public void testSetOwnerIdWhenOwnerIsNotNull() {
    User owner = new User();
    Chat chat = new Chat();
    chat.setOwner(owner);

    chat.setOwnerId(OWNER_ID);
    assertEquals(OWNER_ID, chat.getOwner().getId().longValue());
  }

}
