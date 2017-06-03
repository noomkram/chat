package com.ora.assessment.chat.message;

import static com.ora.assessment.TestUtils.CHAT_ID;
import static com.ora.assessment.TestUtils.EMAIL;
import static com.ora.assessment.TestUtils.MESSAGE;
import static com.ora.assessment.TestUtils.MESSAGE_ID;
import static com.ora.assessment.TestUtils.NAME;
import static com.ora.assessment.TestUtils.USER_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.ora.assessment.user.User;

public class MessageResourceTest {

  public static final Date CREATED = new Date();

  private Message message;
  private User user;

  @Before
  public void setup() {
    user = new User();
    user.setEmail(EMAIL);
    user.setId(USER_ID);
    user.setName(NAME);

    message = new Message();
    message.setChatId(CHAT_ID);
    message.setCreated(CREATED);
    message.setId(MESSAGE_ID);
    message.setMessage(MESSAGE);
    message.setUser(user);
  }

  @Test
  public void testConstructor() {
    MessageResource actual = new MessageResource(message);
    assertEquals(CHAT_ID, actual.getChatId());
    assertEquals(CREATED, actual.getCreated());
    assertEquals(MESSAGE_ID, actual.getId());
    assertEquals(MESSAGE, actual.getMessage());
    assertEquals(USER_ID, actual.getUserId());
    assertNotNull(actual.getUser());
  }

}
