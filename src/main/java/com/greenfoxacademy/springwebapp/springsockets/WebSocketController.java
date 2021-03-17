package com.greenfoxacademy.springwebapp.springsockets;

import com.greenfoxacademy.springwebapp.globalexceptionhandling.IdNotFoundException;
import com.greenfoxacademy.springwebapp.kingdom.models.dtos.KingdomResponseDTO;
import com.greenfoxacademy.springwebapp.kingdom.services.KingdomService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class WebSocketController {

  private final KingdomService kingdomService;

  @SubscribeMapping("/kingdoms/get/{kingdomId}")
  public KingdomResponseDTO sendDto(@DestinationVariable Long kingdomId) throws IdNotFoundException {
    return kingdomService.entityToKingdomResponseDTO(kingdomId);
  }

  @SendTo("kingdoms/updates")
  public KingdomResponseDTO updateMeAboutKingdom(KingdomResponseDTO updated) {
    return updated;
  }
}
