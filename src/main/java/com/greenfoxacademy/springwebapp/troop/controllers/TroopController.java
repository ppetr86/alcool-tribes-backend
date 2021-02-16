package com.greenfoxacademy.springwebapp.troop.controllers;

import com.greenfoxacademy.springwebapp.security.CustomUserDetails;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopListResponseDto;
import com.greenfoxacademy.springwebapp.troop.services.TroopService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class TroopController {
  public static final String URI = "/kingdom/troops";

  private final TroopService troopService;

  @GetMapping(TroopController.URI)
  public ResponseEntity<TroopListResponseDto> getTroopsOfKingdom(Authentication auth) {
    return ResponseEntity.ok(troopService.troopsToListDTO(
                    ((CustomUserDetails) auth.getPrincipal()).getKingdom()));
  }
}