package com.greenfoxacademy.springwebapp.troop.controllers;

import com.greenfoxacademy.springwebapp.security.CustomUserDetails;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopResponseDto;
import com.greenfoxacademy.springwebapp.troop.services.TroopService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class TroopController {
  public static final String URI_KINGDOM_TROOPS = "/kingdom/troops";

  private final TroopService troopService;

  @GetMapping(TroopController.URI_KINGDOM_TROOPS)
  public ResponseEntity<TroopResponseDto> getTroopsOfKingdom(Authentication auth) {

    return ResponseEntity.ok(troopService
            .findTroopEntitiesConvertToResponseDTO(
                    ((CustomUserDetails) auth.getPrincipal()).getKingdom()));
  }
}
