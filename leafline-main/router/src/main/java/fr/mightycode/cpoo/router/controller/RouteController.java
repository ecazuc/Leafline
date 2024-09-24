package fr.mightycode.cpoo.router.controller;

import fr.mightycode.cpoo.router.model.Message;
import fr.mightycode.cpoo.router.service.RouterService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class RouteController {

  private final RouterService routerService;

  @MessageMapping("/route")
  public void route(Message message, StompHeaderAccessor accessor) {
    routerService.routeMessage(message, accessor);
  }
}
