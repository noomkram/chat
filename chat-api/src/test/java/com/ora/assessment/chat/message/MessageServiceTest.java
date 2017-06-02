package com.ora.assessment.chat.message;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MessageServiceTest {

  public static final long CHAT_ID = 1L;

  @InjectMocks
  private MessageService service;
  @Mock
  private MessageRepository messageRepo;

  private Message message;

  @Before
  public void setup() {
    message = new Message();

    when(messageRepo.findTopByChatIdOrderByCreatedDesc(anyLong())).thenReturn(message);
    when(messageRepo.findByChatId(anyLong())).thenReturn(asList(message));
  }

  @Test
  public void testGetLastMessageForChat() {
    Message actual = service.getLastMessageForChat(CHAT_ID);
    assertNotNull(actual);

    verify(messageRepo).findTopByChatIdOrderByCreatedDesc(eq(CHAT_ID));
  }


  @Test
  public void getMessagesForChat() {
    List<Message> actual = service.getMessagesForChat(CHAT_ID);
    assertNotNull(actual);

    verify(messageRepo).findByChatId(eq(CHAT_ID));
  }

}
