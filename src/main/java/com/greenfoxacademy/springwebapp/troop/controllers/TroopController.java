package com.greenfoxacademy.springwebapp.troop.controllers;

import com.greenfoxacademy.springwebapp.globalexceptionhandling.ForbiddenException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.InvalidInputException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.NotEnoughResourceException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.security.CustomUserDetails;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopRequestDTO;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopResponseDTO;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(TroopController.URI)
public class TroopController {
  public static final String URI = "/kingdom/troops";
  TroopService troopService;

  public ResponseEntity<?> createTroop(@RequestBody @Valid TroopRequestDTO requestDTO, Authentication auth)
      throws ForbiddenException, InvalidInputException, NotEnoughResourceException {

    KingdomEntity kingdom = ((CustomUserDetails) auth.getPrincipal()).getKingdom();
    TroopResponseDTO responseDTO = troopService.createTroop(kingdom, requestDTO);
    return ResponseEntity.ok(responseDTO);
  }
}
