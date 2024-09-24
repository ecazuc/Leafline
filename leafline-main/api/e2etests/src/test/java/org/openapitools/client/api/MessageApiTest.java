package org.openapitools.client.api;
import okhttp3.OkHttpClient;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.model.MessageDTO;
import org.openapitools.client.model.NewMessageDTO;
import org.openapitools.client.model.UserDTO;

import java.util.List;
import java.util.UUID;

/**
 * API tests for MessageApi
 */
public class MessageApiTest {

  private final AuthenticationApi authenticationApi = new AuthenticationApi();
  private final MessageApi messageApi = new MessageApi();

  @BeforeEach
  @SuppressWarnings("KotlinInternalInJava")
  public void init() {

    // Simulate the behavior of a web browser by remembering cookies set by the server
    OkHttpClient.Builder builder = new OkHttpClient.Builder();
    OkHttpClient okHttpClient = builder.cookieJar(new MyCookieJar()).build();
    ApiClient apiClient = new ApiClient(okHttpClient);
    messageApi.setApiClient(apiClient);
    authenticationApi.setApiClient(apiClient);
  }

//  @Test
//  public void messagePostTest() throws ApiException, InterruptedException {
//
//    // Posting messages while not signed in should fail with FORBIDDEN
//    try {
//      messageApi.messagePost(new NewMessageDTO().to("bob@acme").type("text/plain").body("This is a test"));
//      Assertions.fail();
//    }
//    catch (ApiException e) {
//      Assertions.assertEquals(HttpStatus.SC_FORBIDDEN, e.getCode());
//    }
//
//    // Sign in
//    authenticationApi.userSigninPost(new UserDTO().login("tester").password("tester"));
//
//    // Get all messages
//    List<MessageDTO> messagesBefore = messageApi.messagesGet();
//
//    // Post a new message
//    NewMessageDTO newMessage = new NewMessageDTO().to("bob@acme").type("text/plain").body("This is a test");
//    messageApi.messagePost(newMessage);
//
//    // Wait for the message to be delivered
//    Thread.sleep(1000L);
//
//    // Get all messages
//    List<MessageDTO> messagesAfter = messageApi.messagesGet();
//
//    // We should have one more message
//    Assertions.assertEquals(messagesAfter.size(), messagesBefore.size() + 1);
//
//    // The first message should be our new message (since it is the most recent)
//    MessageDTO firstMessage = messagesAfter.get(0);
//    Assertions.assertEquals("tester@acme", firstMessage.getFrom());
//    Assertions.assertEquals(newMessage.getTo(), firstMessage.getTo());
//    Assertions.assertEquals(newMessage.getType(), firstMessage.getType());
//    Assertions.assertEquals(newMessage.getBody(), firstMessage.getBody());
//    Assertions.assertNotNull(firstMessage.getId());
//    Assertions.assertTrue(firstMessage.getTimestamp() > System.currentTimeMillis() - 2000L
//      && firstMessage.getTimestamp() < System.currentTimeMillis() + 2000L);
//  }

//  @Test
//  public void messagesGetTest() throws ApiException {
//
//    // Getting messages while not signed in should fail with FORBIDDEN
//    try {
//      messageApi.messagesGet();
//      Assertions.fail();
//    }
//    catch (ApiException e) {
//      Assertions.assertEquals(HttpStatus.SC_FORBIDDEN, e.getCode());
//    }
//
//    // Sign in
//    authenticationApi.userSigninPost(new UserDTO().login("alice").password("alice"));
//
//    // Get all messages
//    messageApi.messagesGet();
//  }
}

