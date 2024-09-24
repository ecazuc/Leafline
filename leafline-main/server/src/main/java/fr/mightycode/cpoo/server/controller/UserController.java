package fr.mightycode.cpoo.server.controller;


import fr.mightycode.cpoo.server.dto.ProfilDTO;
import fr.mightycode.cpoo.server.dto.UserDTO;
import fr.mightycode.cpoo.server.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RestController
@RequestMapping("user")
@CrossOrigin
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  @PostMapping(value = "signup", consumes = MediaType.APPLICATION_JSON_VALUE)
  public void signup(@RequestBody final UserDTO user) {
    if (!userService.signup(user.login(), user.password()))
      throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
  }

  @PostMapping(value = "signin", consumes = MediaType.APPLICATION_JSON_VALUE)
  public void signin(@RequestBody final UserDTO user) {
    try {
      if (!userService.signin(user.login(), user.password()))
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Already signed in");
    }
    catch (final ServletException ex) {
      if (ex.getCause() instanceof BadCredentialsException)
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials");
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
    }
  }

  @GetMapping(value = "profile", produces = MediaType.APPLICATION_JSON_VALUE)
  public ProfilDTO profile(Principal user) {
    return new ProfilDTO(user.getName());
  }

  @PostMapping(value = "signout")
  public void signout() {
    try {
      userService.signout();
    }
    catch (final ServletException ex) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
    }
  }

  @DeleteMapping(value = "{login}")
  public void delete(@PathVariable("login") String login) {
    if (!userService.delete(login))
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
  }
}

