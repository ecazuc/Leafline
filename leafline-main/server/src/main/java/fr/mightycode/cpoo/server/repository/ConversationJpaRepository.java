package fr.mightycode.cpoo.server.repository;

import fr.mightycode.cpoo.server.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ConversationJpaRepository extends JpaRepository<Conversation, UUID>{
    @Query("SELECT c FROM Conversation c WHERE c.participant1 = :username OR c.participant2 = :username")
    List<Conversation> findAllConversationsForUser(@Param("username") String username);

    @Query("SELECT c FROM Conversation c WHERE c.participant1 = :username1 AND c.participant2 = :username2")
    Conversation findConversationBetweenUsers(@Param("username1") String username1, @Param("username2") String username2);
}
