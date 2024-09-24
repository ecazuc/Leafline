package fr.mightycode.cpoo.server.service;


import fr.mightycode.cpoo.server.model.LeaflineUser;
import fr.mightycode.cpoo.server.repository.LeaflineUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LeaflineUserDetailManager implements UserDetailsManager {

  private final LeaflineUserRepository leaflineUserRepository;

  public LeaflineUserDetailManager(LeaflineUserRepository leaflineUserRepository, PasswordEncoder passwordEncoder) {
    this.leaflineUserRepository = leaflineUserRepository;

    try {
      // Create a user account to be used by end-to-end tests
      UserDetails tester = User.withUsername("tester")
        .password(passwordEncoder.encode("tester"))
        .roles("USER")
        .build();
      createUser(tester);
    }
    catch (Exception e) {
      log.info("'tester' account already created");
    }

    try {
      // Create an administrator account
      UserDetails admin = User.withUsername("admin")
        .password(passwordEncoder.encode("admin"))
        .roles("USER", "ADMIN")
        .build();
      createUser(admin);
    }
    catch (Exception e) {
      log.info("'admin' account already created");
    }

    try {
      // Create a user account to use for testing
      UserDetails alice = User.withUsername("alice")
        .password(passwordEncoder.encode("alice"))
        .roles("USER")
        .build();
      createUser(alice);
    }
    catch (Exception e) {
      log.info("'alice' account already created");
    }

    try {
      // Create a user account to use for testing
      UserDetails bob = User.withUsername("bob")
        .password(passwordEncoder.encode("bob"))
        .roles("USER")
        .build();
      createUser(bob);
    }
    catch (Exception e) {
      log.info("'bob' already created");
    }
  }

  @Override
  public void createUser(UserDetails user) {
    LeaflineUser leaflineUser = new LeaflineUser();
    leaflineUser.setLogin(user.getUsername());
    leaflineUser.setPassword(user.getPassword());
    leaflineUserRepository.save(leaflineUser);
  }

  @Override
  public void updateUser(UserDetails user) {
    LeaflineUser leaflineUser = leaflineUserRepository.findByLogin(user.getUsername());
    leaflineUser.setLogin(user.getUsername());
    leaflineUser.setPassword(user.getPassword());
    leaflineUserRepository.save(leaflineUser);
  }

  @Override
  public void deleteUser(String username) {
    leaflineUserRepository.delete(leaflineUserRepository.findByLogin(username));
  }

  @Override
  public void changePassword(String oldPassword, String newPassword) {
  }

  @Override
  public boolean userExists(String username) {
    return leaflineUserRepository.existsByLogin(username);
  }

  // Implementation of the UserDetailsService interface

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    LeaflineUser leaflineUser = leaflineUserRepository.findByLogin(username);
    if (leaflineUser == null)
      throw new UsernameNotFoundException(username);
    return new LeaflineUserDetails(leaflineUser);
  }
}
