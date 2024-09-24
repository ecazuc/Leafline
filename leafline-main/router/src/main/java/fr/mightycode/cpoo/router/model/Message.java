package fr.mightycode.cpoo.router.model;

import lombok.Data;

import java.util.UUID;

import java.util.UUID;

@Data
public class Message {
  private UUID id;      // unique id of the message
  private String from;  // sender address
  private String to;    // recipient address
  private String body;  // body (BASE64 encoded for binary types)
}
