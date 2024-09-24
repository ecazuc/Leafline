package fr.mightycode.cpoo.router.service;

import fr.mightycode.cpoo.router.model.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class RouterService {

  private final SimpMessagingTemplate messagingTemplate;

  /**
   * Check if an address is valid.
   *
   * @param address The address to check
   * @return true is the address is valid
   */
  private boolean isValidAddress(String address) {
    return Pattern.compile("^(.+)@(\\S+)$").matcher(address).matches();
  }

  public void routeMessage(Message message, StompHeaderAccessor accessor) {

    // Check sender and recipient addresses
    if (!isValidAddress(message.getFrom())) {
      log.error("Invalid address 'from': '{}'", message.getFrom());
      return;
    }
    if (!isValidAddress(message.getTo())) {
      log.error("Invalid address 'to': '{}'", message.getTo());
      return;
    }

    // Complete UUID if not set
    if (message.getId() == null)
      message.setId(UUID.randomUUID());

    // Complete timestamp if not set
    if (message.getTimestamp() == 0)
      message.setTimestamp(System.currentTimeMillis());

    log.info("Routing message {}", message);
    String destinationDomain = message.getTo().split("@")[1];
    messagingTemplate.convertAndSend("/domain/" + destinationDomain + "/messages", message);
  }
}
