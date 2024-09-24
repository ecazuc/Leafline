package fr.mightycode.cpoo.server.controller;

import fr.mightycode.cpoo.server.dto.ConfigDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("config")
@CrossOrigin
@RequiredArgsConstructor
public class ConfigController {

  @Value("${cpoo.server.domain}")
  private String serverDomain;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ConfigDTO configGet() {
    return new ConfigDTO(serverDomain);
  }
}
