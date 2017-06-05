package com.ora.assessment.chat;

import static com.ora.assessment.TestUtils.CHAT_ID;
import static com.ora.assessment.TestUtils.MESSAGE;
import static com.ora.assessment.TestUtils.NAME;
import static com.ora.assessment.TestUtils.USER_ID;
import static com.ora.assessment.TestUtils.pageable;
import static com.ora.assessment.TestUtils.populateId;
import static com.ora.assessment.TestUtils.user;
import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;

import com.ora.assessment.NotFoundException;
import com.ora.assessment.chat.message.Message;
import com.ora.assessment.chat.message.MessageService;
import com.ora.assessment.user.User;
import com.ora.assessment.user.UserService;

@RunWith(MockitoJUnitRunner.class)
public class ChatServiceTest {

  public static final long TOTAL = 1;

  @InjectMocks
  private ChatService service;
  @Mock
  private ChatRepository chatRepo;
  @Mock
  private MessageService messageService;
  @Mock
  private UserService userService;

  @Before
  public void setup() {
    when(chatRepo.save(any(Chat.class))).then(populateId);
    when(chatRepo.findOne(anyLong())).thenReturn(chatToUpdate());
    when(chatRepo.count()).thenReturn(TOTAL);
    when(chatRepo.find(any())).thenReturn(asList(new Chat()));
    when(messageService.save(any(Message.class))).then(populateId);
    when(messageService.getLastMessageForChat(anyLong())).thenReturn(newMessage());
    when(userService.getUsersInChat(anyLong())).thenReturn(singleton(user()));
  }

  @Test
  public void testCreate() {
    final Chat expectedChat = chatToCreate();
    final Chat actual = service.save(expectedChat);

    final Long actualChatId = actual.getId();
    assertNotNull(actualChatId);

    final Message actualMessage = actual.getMessage();
    assertEquals(actualChatId, actualMessage.getChatId());

    final Long actualMessageId = actualMessage.getId();
    assertNotNull(actualMessageId);

    final Set<User> actualUsers = actual.getUsers();
    assertEquals(1, actualUsers.size());
    assertTrue(actualUsers.contains(user()));

    verify(userService).getUsersInChat(actualChatId);
  }

  @Test(expected = NotFoundException.class)
  public void testUpdateWhenChatNotFound() {
    when(chatRepo.findOne(CHAT_ID)).thenReturn(null);

    service.save(chatToUpdate());
  }

  @Test(expected = IllegalStateException.class)
  public void testUpdateWhenNotOwnedByUser() {
    final Chat chatWithDifferentOwner = chatToUpdate();
    chatWithDifferentOwner.getOwner().setId(USER_ID + 1);

    when(chatRepo.findOne(CHAT_ID)).thenReturn(chatToUpdate());

    service.save(chatWithDifferentOwner);
  }

  @Test
  public void testUpdate() {
    final String expectedName = "expectedName";
    final Chat expectedChat = chatToUpdate();
    expectedChat.setName(expectedName);
    when(chatRepo.save(any(Chat.class))).thenAnswer(returnsFirstArg());

    final Chat actualChat = service.save(expectedChat);
    assertEquals(expectedName, actualChat.getName());
    assertNotNull(actualChat.getMessage());

    final Set<User> actualUsers = actualChat.getUsers();
    assertEquals(1, actualUsers.size());
    assertTrue(actualUsers.contains(user()));

    verify(messageService).getLastMessageForChat(CHAT_ID);
    verify(userService).getUsersInChat(CHAT_ID);
  }

  @Test
  public void testGet() {
    Page<Chat> actual = service.get(pageable());
    assertNotNull(actual.getContent());
    assertEquals(TOTAL, actual.getTotalElements());

    verify(chatRepo).find(pageable());
  }

  private Chat chatToCreate() {
    Chat c = new Chat();
    c.setMessage(newMessage());
    c.setName(NAME);
    c.setOwner(user());
    return c;
  }

  private Chat chatToUpdate() {
    Chat c = chatToCreate();
    c.setId(CHAT_ID);
    return c;
  }

  private Message newMessage() {
    Message message = new Message();
    message.setMessage(MESSAGE);
    return message;
  }

}
