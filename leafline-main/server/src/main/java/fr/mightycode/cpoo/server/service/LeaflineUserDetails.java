package fr.mightycode.cpoo.server.service;

import fr.mightycode.cpoo.server.model.LeaflineUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class LeaflineUserDetails implements UserDetails {

  private final LeaflineUser leaflineUser;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<GrantedAuthority> roles = new ArrayList<>();
    roles.add(new SimpleGrantedAuthority("ROLE_USER"));
    if ("admin".equals(leaflineUser.getLogin()))
      roles.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
    return roles;
  }

  @Override
  public String getPassword() {
    return leaflineUser.getPassword();
  }

  @Override
  public String getUsername() {
    return leaflineUser.getLogin();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
