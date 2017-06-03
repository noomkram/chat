package com.ora.assessment.chat;

import static com.ora.assessment.TestUtils.CHAT_ID;
import static com.ora.assessment.TestUtils.OWNER_ID;
import static com.ora.assessment.TestUtils.populateId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.ora.assessment.NotFoundException;
import com.ora.assessment.chat.message.Message;
import com.ora.assessment.chat.message.MessageService;
import com.ora.assessment.user.User;

@RunWith(MockitoJUnitRunner.class)
public class ChatServiceTest {

  public static final String NAME = "NAME";
  public static final String MESSAGE = "MESSAGE";

  @InjectMocks
  private ChatService service;
  @Mock
  private ChatRepository chatRepo;
  @Mock
  private MessageService messageService;

  @Before
  public void setup() {
    when(chatRepo.save(any(Chat.class))).then(populateId);
    when(chatRepo.findOne(anyLong())).thenReturn(chatToUpdate());
    when(messageService.save(any(Message.class))).then(populateId);
    when(messageService.getLastMessageForChat(anyLong())).thenReturn(newMessage());
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
  }

  @Test(expected = NotFoundException.class)
  public void testUpdateWhenChatNotFound() {
    when(chatRepo.findOne(CHAT_ID)).thenReturn(null);

    service.save(chatToUpdate());
  }

  @Test(expected = IllegalStateException.class)
  public void testUpdateWhenNotOwnedByUser() {
    final Chat chatWithDifferentOwner = chatToUpdate();
    chatWithDifferentOwner.getOwner().setId(OWNER_ID + 1);

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

    verify(messageService).getLastMessageForChat(CHAT_ID);
  }

  private Chat chatToCreate() {
    User owner = new User();
    owner.setId(OWNER_ID);

    Chat c = new Chat();
    c.setMessage(newMessage());
    c.setName(NAME);
    c.setOwner(owner);
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
