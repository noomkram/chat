package com.ora.assessment.chat.message;

import static com.ora.assessment.TestUtils.CHAT_ID;
import static com.ora.assessment.TestUtils.MESSAGE;
import static com.ora.assessment.TestUtils.USER_ID;
import static com.ora.assessment.TestUtils.authenticatedUser;
import static com.ora.assessment.TestUtils.user;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import java.util.Date;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.ora.assessment.TestUtils;
import com.ora.assessment.resource.DataListResource;
import com.ora.assessment.resource.DataResource;

@RunWith(MockitoJUnitRunner.class)
public class MessageControllerTest {

  @InjectMocks
  private MessageController controller;
  @Mock
  private MessageService messageService;
  @Captor
  private ArgumentCaptor<Message> messageCaptor;

  @Test
  public void testCreate() {
    when(messageService.save(any())).thenAnswer(invocation -> {
      Message message = invocation.getArgument(0);
      message.setId(new Random().nextLong());
      message.setUser(user());
      return message;
    });

    ResponseEntity<DataResource<MessageResource>> actualResponse =
        controller.create(authenticatedUser(), CHAT_ID, saveMessage());

    assertEquals(CREATED, actualResponse.getStatusCode());

    verify(messageService).save(messageCaptor.capture());
    Message actualMessage = messageCaptor.getValue();
    assertNotNull(actualMessage.getId());
    assertEquals(USER_ID, actualMessage.getUser().getId().longValue());
    assertEquals(CHAT_ID, actualMessage.getChatId().longValue());
    assertEquals(MESSAGE, actualMessage.getMessage());

    MessageResource actualMessageResource = actualResponse.getBody().getData();
    assertNotNull(actualMessageResource);
  }

  @Test
  public void testGetMessages() {
    Pageable pageable = new PageRequest(1, 1);

    when(messageService.getMessagesForChat(eq(CHAT_ID), any())).thenReturn(messages());


    ResponseEntity<DataListResource> actualResponse = controller.getMessages(CHAT_ID, pageable);

    assertEquals(OK, actualResponse.getStatusCode());
    assertNotNull(actualResponse.getBody());

    verify(messageService).getMessagesForChat(CHAT_ID, pageable);
  }

  private SaveMessage saveMessage() {
    SaveMessage message = new SaveMessage();
    message.setMessage(MESSAGE);
    return message;
  }

  private Message message() {
    Message message = new Message();
    message.setChatId(CHAT_ID);
    message.setCreated(new Date());
    message.setId(TestUtils.MESSAGE_ID);
    message.setMessage(MESSAGE);
    message.setUser(user());
    return message;
  }

  private Page<Message> messages() {
    Page<Message> messages = new PageImpl<>(asList(message()));

    return messages;
  }

}
