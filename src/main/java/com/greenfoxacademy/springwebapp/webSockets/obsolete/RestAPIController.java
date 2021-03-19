/*
package com.greenfoxacademy.springwebapp.webSockets.obsolete;

import com.greenfoxacademy.springwebapp.globalexceptionhandling.IdNotFoundException;
import com.greenfoxacademy.springwebapp.kingdom.models.dtos.KingdomResponseDTO;
import com.greenfoxacademy.springwebapp.kingdom.services.KingdomService;
import com.greenfoxacademy.springwebapp.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.InputMismatchException;

@RestController
@RequestMapping(path = "/rest")
@RequiredArgsConstructor
@Slf4j
public class RestAPIController {

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

  @MessageExceptionHandler
  @SendToUser("/kingdoms/error")
  public String handleException(IdNotFoundException ex) {
    log.debug("Post not found", ex);
    return "The requested kingdomID was not found";
  }
}
*/
