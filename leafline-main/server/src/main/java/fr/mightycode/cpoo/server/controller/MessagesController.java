package fr.mightycode.cpoo.server.controller;

import fr.mightycode.cpoo.server.dto.MessageDTO;
import fr.mightycode.cpoo.server.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("messages")
@CrossOrigin
@RequiredArgsConstructor
public class MessagesController {

  private final MessageService messageService;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public List<MessageDTO> messagesGet(final Principal user) {
    return messageService.getMessagesOf(user.getName()).stream().map(message -> new MessageDTO(message)).toList();
  }
}

