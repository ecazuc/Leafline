package fr.mightycode.cpoo.server.model;

import fr.mightycode.cpoo.server.service.RouterService;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Table(name = "messages")
@NoArgsConstructor
public class Message {

  @Id
  private UUID id;        // unique id of the message

  @Column(name = "timestamp", nullable = false)
  private long timestamp; // timestamp of the message

  @Column(name = "`from`", nullable = false)
  private String from;    // sender address

  @Column(name = "`to`", nullable = false)
  private String to;      // recipient address

  @Column(name = "type", nullable = false)
  private String type;    // MIME type of the body

  @Column(name = "body", nullable = false, length = 2000)
  private String body;    // body (BASE64 encoded for binary types)

  public Message(RouterService.Message routerMessage) {
    this.id = routerMessage.id();
    this.timestamp = routerMessage.timestamp();
    this.from = routerMessage.from();
    this.to = routerMessage.to();
    this.type = routerMessage.type();
    this.body = routerMessage.body();
  }
}


