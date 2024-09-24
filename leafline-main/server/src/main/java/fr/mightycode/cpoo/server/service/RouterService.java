package fr.mightycode.cpoo.server.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Validated
public interface RouterService {

  /**
   * Type of messages exchanged between domain servers.
   */
  record Message(
    @NotNull
    UUID id,        // unique id of the message
    @Positive
    long timestamp, // timestamp of the message
    @NotEmpty @Email
    String from,    // sender address
    @NotEmpty @Email
    String to,      // recipient address
    @NotEmpty
    String type,    // MIME type of the body
    @NotEmpty
    String body     // body (BASE64 encoded for binary types)
  ) {
  }

  /**
   * This interface is used by the router service to notify the domain server about incoming messages.
   * It must be implemented as a @Component or @Service, so that it can be injected automatically at service
   * creation time.
   */
  interface MessageListener {

    /**
     * @return The name of the domain to listen.
     */
    String getServerDomain();

    /**
     * @return The URL of the WS router.
     */
    String getRouterWSUrl();

    /**
     * @return The URL of the SSE router.
     */
    String getRouterSSEUrl();

    /**
     * Notify the listener about an incoming message for its domain.
     *
     * @param message The incoming message.
     */
    void onMessageReceived(Message message);
  }

  /**
   * Route a message to the recipient's domain server
   * (i.e. the domain specified in the 'to' property of the message).
   *
   * @param message The message to route
   */
  void routeMessage(@Valid Message message);
}

