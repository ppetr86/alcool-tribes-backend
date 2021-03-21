package com.greenfoxacademy.springwebapp.webSockets;

import com.greenfoxacademy.springwebapp.globalexceptionhandling.IdNotFoundException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.models.dtos.KingdomResponseDTO;
import com.greenfoxacademy.springwebapp.kingdom.services.KingdomService;
import com.greenfoxacademy.springwebapp.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.security.Principal;

import static com.greenfoxacademy.springwebapp.webSockets.WebSocketConfiguration.CHAT_SPECIFIC_USER;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebSocketController {

  private final KingdomService kingdomService;
  private final SimpMessageSendingOperations messaging;

  //app prefix is implied
  @SubscribeMapping("/kingdom-update/{kingdomId}")
  @SendToUser
  public String handleSubscription(@DestinationVariable Long kingdomId, IncomingMessage msg, Authentication auth)
      throws IdNotFoundException {
    KingdomEntity kingdom = ((CustomUserDetails) auth.getPrincipal()).getKingdom();

    if (!kingdomId.equals(kingdom.getId())) {
      log.info("Provided ID for subscription does not match your kingdom ID");
      return "Provided ID for subscription does not match your kingdom ID";
    }

    boolean setStatus = false;
    if (msg.getMessage().equalsIgnoreCase("yes") || msg.getMessage().equalsIgnoreCase("true") ||
        msg.getMessage().equalsIgnoreCase("1") || msg.getMessage().equalsIgnoreCase("davaj tavarysc")) {
      setStatus = true;
    }
    boolean isSubscribed = kingdomService.setSubscription(kingdom, setStatus);
    String status = isSubscribed ? "subscribed" : "not-subscribed";
    return status;
  }

  @MessageMapping(CHAT_SPECIFIC_USER)
  public void sendSpecific(KingdomResponseDTO out, @Payload Message msg, Principal principal,
                           @Header("simpSessionId") String sessionId) throws Exception {

    messaging.convertAndSendToUser(principal.getName(), CHAT_SPECIFIC_USER, out);
  }

  @MessageExceptionHandler
  @SendToUser("/queue/errors")
  public void handleException(Throwable t) {
    log.error("Error handling message: " + t.getMessage());
  }
}