package com.greenfoxacademy.springwebapp.webSockets;

import com.greenfoxacademy.springwebapp.globalexceptionhandling.ErrorDTO;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.IdNotFoundException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.services.KingdomService;
import com.greenfoxacademy.springwebapp.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebSocketController {

  private final KingdomService kingdomService;

  //The primary use case for @SubscribeMapping is to implement a request-reply pattern.
  //In the request-reply pattern, the client subscribes to a destination expecting a
  //one-time response at that destination.
  @SubscribeMapping("/kingdom-update/{kingdomId}")
  public ErrorDTO handleSubscription(@DestinationVariable Long kingdomId, IncomingMessage msg, Authentication auth)
      throws IdNotFoundException {
    KingdomEntity kingdom = ((CustomUserDetails) auth.getPrincipal()).getKingdom();

    if (!kingdomId.equals(kingdom.getId())) {
      log.info("Provided ID for subscription does not match your kingdom ID");
      return new ErrorDTO("Provided ID for subscription does not match your kingdom ID");
    }

    boolean setStatus = false;
    if (msg.getMessage().equalsIgnoreCase("yes") || msg.getMessage().equalsIgnoreCase("true") ||
        msg.getMessage().equalsIgnoreCase("1") || msg.getMessage().equalsIgnoreCase("davaj tavarysc")) {
      setStatus = true;
    }
    boolean isSubscribed = kingdomService.setSubscription(kingdom, setStatus);
    String status = isSubscribed ? "subscribed" : "not-subscribed";
    return new ErrorDTO("ok", status);
  }
}