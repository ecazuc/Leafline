package fr.mightycode.cpoo.server.repository;

import fr.mightycode.cpoo.server.model.LeaflineUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LeaflineUserRepository extends JpaRepository<LeaflineUser, UUID> {
  LeaflineUser findByLogin(String login);

  boolean existsByLogin(String login);
}
