package com.greenfoxacademy.springwebapp.webSockets.obsolete;

import com.greenfoxacademy.springwebapp.globalexceptionhandling.IdNotFoundException;
import com.greenfoxacademy.springwebapp.kingdom.models.dtos.KingdomResponseDTO;
import com.greenfoxacademy.springwebapp.kingdom.services.KingdomService;
import com.greenfoxacademy.springwebapp.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.util.InputMismatchException;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

  private final KingdomService kingdomService;

  @SubscribeMapping("/kingdoms/get/{kingdomId}")
  public KingdomResponseDTO sendDto(@DestinationVariable Long kingdomId, Authentication auth) throws IdNotFoundException {
    if (!kingdomId.equals(((CustomUserDetails) auth.getPrincipal()).getKingdom().getId()))
      throw new InputMismatchException("Provided ID does not match your kingdom ID");
    return kingdomService.entityToKingdomResponseDTO(kingdomId);
  }

  @SendTo("kingdoms/updates")
  public KingdomResponseDTO updateMeAboutKingdom(KingdomResponseDTO updated) {
    return updated;
  }
}
