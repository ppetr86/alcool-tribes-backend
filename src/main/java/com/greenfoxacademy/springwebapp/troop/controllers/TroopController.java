package com.greenfoxacademy.springwebapp.troop.controllers;

import com.greenfoxacademy.springwebapp.globalexceptionhandling.ForbiddenActionException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.IdNotFoundException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.InvalidAcademyIdException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.NotEnoughResourceException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.security.CustomUserDetails;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopEntityResponseDTO;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopListResponseDto;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopRequestDTO;
import com.greenfoxacademy.springwebapp.troop.services.TroopService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping(TroopController.URI)
public class TroopController {
  public static final String URI = "/kingdom/troops";
  private final TroopService troopService;

  @GetMapping
  public ResponseEntity<TroopListResponseDto> getTroopsOfKingdom(Authentication auth) {
    KingdomEntity kingdom = ((CustomUserDetails) auth.getPrincipal()).getKingdom();
    TroopListResponseDto troops = troopService.troopsToListDTO(kingdom);
    return ResponseEntity.ok(troops);
  }

  @PostMapping
  public ResponseEntity<?> createTroop(@RequestBody @Valid TroopRequestDTO requestDTO, Authentication auth)
      throws ForbiddenActionException, InvalidAcademyIdException, NotEnoughResourceException {

    KingdomEntity kingdom = ((CustomUserDetails) auth.getPrincipal()).getKingdom();

    TroopEntityResponseDTO responseDTO = troopService.createTroop(kingdom, requestDTO);

    return ResponseEntity.ok(responseDTO);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> returnTroop(@PathVariable("id") Long troopId, Authentication auth)
      throws ForbiddenActionException, IdNotFoundException {

    KingdomEntity kingdom = ((CustomUserDetails) auth.getPrincipal()).getKingdom();

    TroopEntityResponseDTO responseDTO = troopService.getTroop(kingdom, troopId);

    return ResponseEntity.ok(responseDTO);
  }
}