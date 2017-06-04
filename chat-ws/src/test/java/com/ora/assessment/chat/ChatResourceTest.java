package com.ora.assessment.chat;

import static com.ora.assessment.TestUtils.CHAT_ID;
import static com.ora.assessment.TestUtils.NAME;
import static com.ora.assessment.TestUtils.USER_ID;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.ora.assessment.user.User;
import com.ora.assessment.user.UserResource;

public class ChatResourceTest {

  private Chat chat;
  private User user;

  @Before
  public void setup() {
    chat = new Chat();
    chat.setId(CHAT_ID);
    chat.setName(NAME);

    user = new User();
    user.setId(USER_ID);
  }

  @Test
  public void testConstructor() {
    final ChatResource actual = new ChatResource(chat);
    assertEquals(CHAT_ID, actual.getId());
    assertEquals(NAME, actual.getName());
    assertTrue(actual.getUsers().isEmpty());
  }

  @Test
  public void testAddUser() {
    final UserResource userResource = new UserResource(user);
    final ChatResource actual = new ChatResource(chat);

    actual.add(userResource);

    assertEquals(userResource, actual.getUsers().get(0));
  }

  @Test
  public void testAddUsers() {
    final UserResource userResource = new UserResource(user);
    final ChatResource actual = new ChatResource(chat);

    actual.add(asList(userResource));

    assertEquals(userResource, actual.getUsers().get(0));
  }


}
