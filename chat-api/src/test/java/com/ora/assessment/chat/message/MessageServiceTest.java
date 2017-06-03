package com.ora.assessment.chat.message;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
    when(messageRepo.findByChatId(anyLong(), any())).thenReturn(new PageImpl<>(asList(message)));
  }

  @Test
  public void testGetLastMessageForChat() {
    final Message actual = service.getLastMessageForChat(CHAT_ID);
    assertNotNull(actual);

    verify(messageRepo).findTopByChatIdOrderByCreatedDesc(eq(CHAT_ID));
  }


  @Test
  public void getMessagesForChat() {
    final Pageable pageable = new PageRequest(1, 50);
    final Page<Message> actual = service.getMessagesForChat(CHAT_ID, pageable);
    assertNotNull(actual);

    verify(messageRepo).findByChatId(eq(CHAT_ID), eq(pageable));
  }

}
