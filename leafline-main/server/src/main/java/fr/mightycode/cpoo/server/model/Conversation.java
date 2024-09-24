package fr.mightycode.cpoo.server.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.mightycode.cpoo.server.dto.ConversationDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;


@NoArgsConstructor
@Data
@Entity
@Table(name = "conversation")
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected UUID id;

    @Column(name = "participant1")
    protected String participant1;

    @Column(name = "participant2")
    protected String participant2;

    @OneToMany
    private List<Message> messages;

    public void addMessage(Message m){
        messages.add(m);
    }

    public Conversation(ConversationDTO t) {
        this.id = t.id();
        this.participant1 = t.participant1();
        this.participant2 = t.participant2();
        this.messages = t.messages();
    }


}
