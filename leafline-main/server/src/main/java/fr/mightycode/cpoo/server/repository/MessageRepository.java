package fr.mightycode.cpoo.server.repository;

import fr.mightycode.cpoo.server.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {
  List<Message> findByFromOrToIgnoreCaseOrderByTimestamp(String from, String to);
}
