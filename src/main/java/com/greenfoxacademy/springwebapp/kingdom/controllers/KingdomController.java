package com.greenfoxacademy.springwebapp.kingdom.controllers;

import com.greenfoxacademy.springwebapp.battle.models.dtos.BattleRequestDTO;
import com.greenfoxacademy.springwebapp.battle.models.dtos.BattleResponseDTO;
import com.greenfoxacademy.springwebapp.battle.services.BattleService;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.ForbiddenActionException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.IdNotFoundException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.MissingParameterException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.models.dtos.KingdomNameDTO;
import com.greenfoxacademy.springwebapp.kingdom.services.KingdomService;
import com.greenfoxacademy.springwebapp.resource.models.dtos.ResourceListResponseDTO;
import com.greenfoxacademy.springwebapp.resource.services.ResourceService;
import com.greenfoxacademy.springwebapp.security.CustomUserDetails;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping(KingdomController.URI)
public class KingdomController {

  public static final String URI = "/kingdom";

  private final KingdomService kingdomService;
  private final ResourceService resourceService;
  private final BattleService battleService;

  @GetMapping("/{id}")
  public ResponseEntity<Object> getKingdomByID(@PathVariable Long id) throws IdNotFoundException {
    return ResponseEntity.ok(kingdomService.entityToKingdomResponseDTO(id));
  }

  @GetMapping("/resources")
  public ResponseEntity<?> getKingdomResources(Authentication authentication) {
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    ResourceListResponseDTO allResources = resourceService.convertKingdomResourcesToListResponseDTO(kingdom);
    return ResponseEntity.ok().body(allResources);
  }

  @PutMapping
  public ResponseEntity<?> updateKingdomByName(Authentication auth,
                                               @RequestBody @Valid KingdomNameDTO nameDTO) {
    KingdomEntity kingdom = ((CustomUserDetails) auth.getPrincipal()).getKingdom();
    return ResponseEntity.ok(kingdomService.changeKingdomName(kingdom, nameDTO));
  }

  @PostMapping("/{id}/battle")
  public ResponseEntity<?> initiateBattle(@PathVariable("id") Long enemyKingdomId,
                                          @RequestBody @Valid BattleRequestDTO requestDTO,
                                          Authentication authentication) throws
      MissingParameterException,
      IdNotFoundException,
      ForbiddenActionException {

    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();

    BattleResponseDTO battleHasStarted = battleService.war(enemyKingdomId, requestDTO, kingdom);

    return ResponseEntity.ok().body(battleHasStarted);
  }
}