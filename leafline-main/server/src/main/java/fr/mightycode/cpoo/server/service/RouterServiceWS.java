package fr.mightycode.cpoo.server.service;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;

@Service
@Slf4j
public class RouterServiceWS implements RouterService {

  @SuppressWarnings("NullableProblems")
  @Slf4j
  public static class RouterStompSessionHandler extends StompSessionHandlerAdapter {

    private final WebSocketStompClient webSocketStompClient;
    private final MessageListener messageListener;

    @Getter
    private StompSession stompSession;

    RouterStompSessionHandler(WebSocketStompClient webSocketStompClient, MessageListener messageListener) {

      // Configure the WebSocket Stomp client
      ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
      taskScheduler.afterPropertiesSet();
      webSocketStompClient.setTaskScheduler(taskScheduler); // for heartbeats
      webSocketStompClient.setDefaultHeartbeat(new long[]{10000, 10000});
      this.webSocketStompClient = webSocketStompClient;

      // Remember the message listener to notify about incoming messages
      this.messageListener = messageListener;
    }

    /**
     * Try to reconnect to the router after a delay.
     */
    @SneakyThrows
    private void reconnect() {
      log.info("Trying to reconnect to the WS router {}...", messageListener.getRouterWSUrl());
      Thread.sleep(2000);
      webSocketStompClient.connectAsync(messageListener.getRouterWSUrl(), this);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
      if (MimeTypeUtils.APPLICATION_JSON.equals(headers.getContentType()))
        return Message.class;
      throw new RuntimeException("Unexpected message type " + headers.getContentType());
    }

    @Override
    public void afterConnected(StompSession stompSession, StompHeaders headers) {
      log.info("WS client connected: stompSession {} headers {}", stompSession, headers);
      this.stompSession = stompSession;
      String destination = "/domain/" + messageListener.getServerDomain() + "/messages";
      log.info("Subscribing to destination {}", destination);
      stompSession.subscribe(destination, this);
    }

    @Override
    public void handleFrame(StompHeaders headers, @Nullable Object payload) {
      log.debug("WS client frame: headers {} payload {}", payload, headers);
      messageListener.onMessageReceived((Message) payload);
    }

    @Override
    public void handleException(StompSession stompSession, @Nullable StompCommand command, StompHeaders headers,
                                byte[] payload, Throwable exception) {
      log.error("WS client exception: {}", exception.getMessage());
      if (!stompSession.isConnected())
        reconnect();
    }

    @Override
    public void handleTransportError(StompSession stompSession, Throwable exception) {
      log.error("WS client transport error: {}", exception.getMessage());
      if (!stompSession.isConnected())
        reconnect();
    }
  }

  private final RouterStompSessionHandler routerStompSessionHandler;

  RouterServiceWS(MessageListener messageListener) {

    // Create the WebSocket client and the Stomp sessions handler
    WebSocketStompClient webSocketStompClient = new WebSocketStompClient(new StandardWebSocketClient());
    webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());
    routerStompSessionHandler = new RouterStompSessionHandler(webSocketStompClient, messageListener);

    // Attempt to connect immediately
    log.info("Opening WS connection with router {} for domain {}", messageListener.getRouterWSUrl(),
      messageListener.getServerDomain());
    webSocketStompClient.connectAsync(messageListener.getRouterWSUrl(), routerStompSessionHandler);
  }

  @Override
  public void routeMessage(Message message) {
    log.info("Routing message using WS {}", message);
    StompSession stompSession = routerStompSessionHandler.getStompSession();
    if (!stompSession.isConnected()) {
      log.error("Not connected to router");
      return;
    }
    stompSession.send("/router/route", message);
  }
}
