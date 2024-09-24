package fr.mightycode.cpoo.server.service;

import fr.mightycode.cpoo.server.model.Message;
import fr.mightycode.cpoo.server.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

  @Value("${cpoo.server.domain}")
  private String serverDomain;

  private final MessageRepository messageRepository;

  // All messages incoming from the router are notified using per recipient sinks,
  // and all messages posted by clients are notified using per sender sinks
  private final Map<String, Sinks.Many<Message>> messageSinks = new HashMap<>();

  /**
   * Get the message sink of a given user (create it if it does not exist).
   *
   * @param address The address of the user
   * @return the message sink of the user
   */
  public Sinks.Many<Message> getMessageSinkFor(String address) {
    Sinks.Many<Message> messageSink = messageSinks.get(address);
    if (messageSink == null) {
      messageSink = Sinks.many().multicast().directBestEffort();
      messageSinks.put(address, messageSink);
    }
    return messageSink;
  }

  /**
   * Store a message in the database.
   *
   * @param message The message to store
   * @return the stored message
   */
  public Message storeMessage(Message message) {
    log.info("Storing message {}", message);
    return messageRepository.save(message);
  }

  /**
   * Notify a user about an incoming message.
   *
   * @param message The message to notify
   * @param address The address of the user
   */
  public void notifyMessageTo(Message message, String address) {
    log.info("Notifying message {} to {}", message, address);
    getMessageSinkFor(address).tryEmitNext(message);
  }

  /**
   * Get all messages related to a given user (ie. send to or by a given user).
   *
   * @param login The user login
   * @return the list of messages sent to or by the user
   */
  public List<Message> getMessagesOf(String login) {
    String address = login + "@" + serverDomain;
    return messageRepository.findByFromOrToIgnoreCaseOrderByTimestamp(address, address);
  }

  public Optional<Message> findById(UUID id) {
    return messageRepository.findById(id);
  }

  public boolean delete(UUID id) {
    Message message = messageRepository.findById(id).orElse(null);
    if (message == null)
      return false;
    messageRepository.delete(message);
    return true;
  }
}

