package com.ora.assessment.chat;

import static com.ora.assessment.TestUtils.CHAT_ID;
import static com.ora.assessment.TestUtils.EMAIL;
import static com.ora.assessment.TestUtils.MESSAGE;
import static com.ora.assessment.TestUtils.MESSAGE_ID;
import static com.ora.assessment.TestUtils.NAME;
import static com.ora.assessment.TestUtils.USER_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;

import com.ora.assessment.GlobalExceptionHandlers;
import com.ora.assessment.chat.message.Message;
import com.ora.assessment.resource.DataResource;
import com.ora.assessment.resource.ErrorResource;
import com.ora.assessment.security.AuthenticatedUser;
import com.ora.assessment.user.User;

@RunWith(MockitoJUnitRunner.class)
public class ChatControllerTest {

  @InjectMocks
  private ChatController controller;
  @Mock
  private ChatService chatService;
  @Captor
  private ArgumentCaptor<Chat> chatCaptor;

  private AuthenticatedUser authenticatedUser;
  private User user;

  @Before
  public void setup() {
    authenticatedUser = new AuthenticatedUser(USER_ID, EMAIL, NAME);
    user = new User();
    user.setId(USER_ID);
  }

  @Test
  public void testCreate() {
    final CreateChat createChat = new CreateChat();
    createChat.setMessage(MESSAGE);
    createChat.setName(NAME);

    when(chatService.save(any())).thenAnswer(invocation -> {
      Random rand = new Random();

      Chat chat = invocation.getArgument(0);
      chat.setId(rand.nextLong());

      chat.getMessage().setId(rand.nextLong());
      chat.getMessage().setChatId(chat.getId());
      return chat;
    });

    final ResponseEntity<DataResource<ChatResource>> actualResponse =
        controller.create(authenticatedUser, createChat);
    assertEquals(OK, actualResponse.getStatusCode());

    verify(chatService).save(chatCaptor.capture());
    final Chat actualChat = chatCaptor.getValue();
    assertNotNull(actualChat.getId());
    assertEquals(NAME, actualChat.getName());
    assertEquals(USER_ID, actualChat.getOwner().getId().longValue());
    assertEquals(MESSAGE, actualChat.getMessage().getMessage());

    final ChatResource actualChatResource = actualResponse.getBody().getData();
    assertNotNull(actualChatResource.getMessage().getId());
  }

  @Test
  public void testUpdate() {
    final String expectedName = "expectedName";
    final UpdateChat updateChat = new UpdateChat();
    updateChat.setName(expectedName);

    when(chatService.save(any())).thenAnswer(invocation -> {
      Chat chat = invocation.getArgument(0);

      Message message = new Message();
      message.setId(MESSAGE_ID);
      message.setMessage(MESSAGE);
      message.setUser(user);
      message.setChatId(chat.getId());

      chat.setMessage(message);
      chat.setOwner(user);

      return chat;
    });

    final ResponseEntity<DataResource<ChatResource>> actualResponse =
        controller.update(authenticatedUser, CHAT_ID, updateChat);
    assertEquals(CREATED, actualResponse.getStatusCode());

    verify(chatService).save(chatCaptor.capture());
    final Chat actualChat = chatCaptor.getValue();
    assertEquals(CHAT_ID, actualChat.getId().longValue());
    assertEquals(expectedName, actualChat.getName());
    assertEquals(USER_ID, actualChat.getOwner().getId().longValue());
    assertEquals(MESSAGE, actualChat.getMessage().getMessage());

    final ChatResource actualChatResource = actualResponse.getBody().getData();
    assertNotNull(actualChatResource);
    assertNotNull(actualChatResource.getUsers());
  }

  @Test
  public void testDuplicateChatName() {
    ErrorResource actual =
        controller.duplicateChatName(new DataIntegrityViolationException(MESSAGE));
    assertEquals(GlobalExceptionHandlers.VAILATION_FAILED, actual.getMessage());
    assertEquals("chat with name already exists", actual.getErrors().get("name").get(0));
  }

}
