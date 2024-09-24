package fr.mightycode.cpoo.server.dto;

import fr.mightycode.cpoo.server.model.Message;

import java.util.List;
import java.util.UUID;

public record ConversationDTO(UUID id, String participant1, String participant2, List<Message> messages) {

  public ConversationDTO(fr.mightycode.cpoo.server.model.Conversation conversation) {
    this(conversation.getId(), conversation.getParticipant1(), conversation.getParticipant2(), conversation.getMessages());
  }
}
