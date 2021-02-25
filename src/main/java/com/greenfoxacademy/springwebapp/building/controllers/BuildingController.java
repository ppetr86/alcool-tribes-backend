package com.greenfoxacademy.springwebapp.building.controllers;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingRequestDTO;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingListResponseDTO;
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.InvalidInputException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.MissingParameterException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.NotEnoughResourceException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.TownhallLevelException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.security.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(BuildingController.URI)
public class BuildingController {

  public static final String URI = "/kingdom/buildings";

  private final BuildingService buildingService;

  @GetMapping
  public ResponseEntity<BuildingListResponseDTO> getKingdomBuildings(Authentication auth) {
    KingdomEntity kingdom = ((CustomUserDetails) auth.getPrincipal()).getKingdom();
    List<BuildingEntity> list = kingdom.getBuildings();
    return ResponseEntity.status(HttpStatus.OK).body(new BuildingListResponseDTO(list));
  }

  @PostMapping
  public ResponseEntity<?> buildBuilding(Authentication auth, @RequestBody @Valid BuildingRequestDTO dto)
          throws InvalidInputException, TownhallLevelException, NotEnoughResourceException, MissingParameterException {
    KingdomEntity kingdom = ((CustomUserDetails) auth.getPrincipal()).getKingdom();
    return ResponseEntity.ok(buildingService.createBuilding(kingdom, dto));
  }
}