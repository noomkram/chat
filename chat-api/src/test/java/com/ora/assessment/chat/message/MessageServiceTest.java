package com.ora.assessment.chat.message;

import static com.ora.assessment.TestUtils.populateId;
import static java.lang.Long.MIN_VALUE;
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

import com.ora.assessment.user.User;
import com.ora.assessment.user.UserService;

@RunWith(MockitoJUnitRunner.class)
public class MessageServiceTest {

  public static final long CHAT_ID = 1L;
  public static final Long USER_ID = 2L;

  @InjectMocks
  private MessageService service;
  @Mock
  private MessageRepository messageRepo;
  @Mock
  private UserService userService;

  @Before
  public void setup() {
    when(messageRepo.findTopByChatIdOrderByCreatedDesc(anyLong())).thenReturn(new Message());
    when(messageRepo.findByChatId(anyLong(), any()))
        .thenReturn(new PageImpl<>(asList(new Message())));
    when(messageRepo.save(any(Message.class))).thenAnswer(populateId);
    when(userService.get(anyLong())).thenReturn(user());
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

  @Test(expected = UnsupportedOperationException.class)
  public void testUpdate() {
    service.save(messageToUpdate());
  }

  @Test
  public void testCreate() {
    final Message actual = service.save(messageToCreate());
    assertNotNull(actual.getId());

    verify(userService).get(USER_ID);
  }

  private Message messageToCreate() {
    Message message = new Message();
    message.setUser(user());
    return message;
  }

  private Message messageToUpdate() {
    Message message = messageToCreate();
    message.setId(MIN_VALUE);
    return message;
  }

  private User user() {
    User user = new User();
    user.setId(USER_ID);
    return user;
  }


}
