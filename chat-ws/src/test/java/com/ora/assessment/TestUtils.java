package com.ora.assessment;

import java.util.Random;

import org.mockito.stubbing.Answer;

import com.ora.assessment.security.AuthenticatedUser;
import com.ora.assessment.security.Token;
import com.ora.assessment.user.User;

public class TestUtils {

  public static final long ID = 1L;
  public static final long OWNER_ID = 2L;
  public static final long USER_ID = 3L;
  public static final long CHAT_ID = 4L;
  public static final long MESSAGE_ID = 5L;
  public static final String EMAIL = "EMAIL";
  public static final String PASSWORD = "PASSWORD";
  public static final String ENCODED_PASSWORD = "ENCODED_PASSWORD";
  public static final String NAME = "NAME";
  public static final String MESSAGE = "MESSAGE";
  public static final String TOKEN = "TOKEN";

  @SuppressWarnings("unchecked")
  public static Answer<Identifiable<Long>> populateId = invocation -> {
    Identifiable<Long> identifiable = (Identifiable<Long>) invocation.getArgument(0);
    identifiable.setId(new Random().nextLong());
    return identifiable;
  };

  public static AuthenticatedUser authenticatedUser() {
    return new AuthenticatedUser(USER_ID, EMAIL, NAME);
  }

  public static User user() {
    User user = new User();
    user.setEmail(EMAIL);
    user.setId(USER_ID);
    user.setName(NAME);
    user.setPassword(PASSWORD);
    return user;
  }

  public static Token token() {
    Token t = new Token();
    t.setToken(TOKEN);
    return t;
  }

}
