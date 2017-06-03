package com.ora.assessment;

import java.util.Random;

import org.mockito.stubbing.Answer;

public class TestUtils {

  public static final long ID = 1L;
  public static final long OWNER_ID = 2L;
  public static final long USER_ID = 3L;
  public static final long CHAT_ID = 4L;
  public static final long MESSAGE_ID = 5L;
  public static final String EMAIL = "EMAIL";
  public static final String PASSWORD = "PASSWORD";
  public static final String ENCODED_PASSWORD = "ENCODED_PASSWORD";

  @SuppressWarnings("unchecked")
  public static Answer<Identifiable<Long>> populateId = invocation -> {
    Identifiable<Long> identifiable = (Identifiable<Long>) invocation.getArgument(0);
    identifiable.setId(new Random().nextLong());
    return identifiable;
  };

}
