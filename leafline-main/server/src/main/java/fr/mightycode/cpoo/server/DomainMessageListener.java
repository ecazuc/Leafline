package fr.mightycode.cpoo.server;

import fr.mightycode.cpoo.server.model.Message;
import fr.mightycode.cpoo.server.service.MessageService;
import fr.mightycode.cpoo.server.service.RouterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DomainMessageListener implements RouterService.MessageListener {

  @Value("${cpoo.server.domain}")
  private String serverDomain;

  @Value("${cpoo.router.ws.url}")
  private String routerWSUrl;

  @Value("${cpoo.router.sse.url}")
  private String routerSSEUrl;

  private final MessageService messageService;

  @Override
  public String getServerDomain() {
    return serverDomain;
  }

  @Override
  public String getRouterWSUrl() {
    return routerWSUrl;
  }

  @Override
  public String getRouterSSEUrl() {
    return routerSSEUrl;
  }

  @Override
  public synchronized void onMessageReceived(RouterService.Message routerMessage) {

    log.info("Message received from router {}", routerMessage);

    // If the message is not already stored (it may have been routed using both WS and SSE)
    Message message = messageService.findById(routerMessage.id()).orElse(null);
    if (message != null) {
      log.warn("Message {} already stored... discarded");
      return;
    }

    // Store the message
    message = messageService.storeMessage(new Message(routerMessage));

    // Notify the message to the recipient (since he is part of the domain)
    messageService.notifyMessageTo(message, message.getTo());

    // Notify the message to the sender if he is part of the domain
    if (message.getFrom().endsWith("@" + serverDomain))
      messageService.notifyMessageTo(message, message.getFrom());
  }
}
