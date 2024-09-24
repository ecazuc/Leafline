package fr.mightycode.cpoo.server.dto;

import fr.mightycode.cpoo.server.model.Message;
import fr.mightycode.cpoo.server.service.RouterService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import java.util.UUID;

public record MessageDTO(@NotEmpty UUID id, long timestamp, @NotEmpty @Email String from, @NotEmpty @Email String to,
                         @NotEmpty String type, @NotEmpty String body) {

  // Build a message DTO from a router message
  public MessageDTO(RouterService.Message message) {
    this(message.id(), message.timestamp(), message.from(), message.to(), message.type(), message.body());
  }

  // Build a message DTO from a model message
  public MessageDTO(Message message) {
    this(message.getId(), message.getTimestamp(), message.getFrom(), message.getTo(), message.getType(), message.getBody());
  }
}


