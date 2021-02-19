package com.greenfoxacademy.springwebapp.building.controllers;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingDetailsDTO;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingRequestDTO;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingResponseDTO;
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.*;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.services.KingdomService;
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
  private KingdomService kingdomService;

  @GetMapping
  public ResponseEntity<BuildingResponseDTO> getKingdomBuildings(Authentication auth) {
    KingdomEntity kingdom = ((CustomUserDetails) auth.getPrincipal()).getKingdom();

    List<BuildingEntity> list = buildingService.findBuildingsByKingdomId(kingdom.getId());

    return ResponseEntity.status(HttpStatus.OK).body(new BuildingResponseDTO(list));
  }

  @PostMapping
  public ResponseEntity<?> buildBuilding(Authentication auth, @RequestBody @Valid BuildingRequestDTO dto)
    throws InvalidInputException, TownhallLevelException, NotEnoughResourceException, MissingParameterException {
    KingdomEntity kingdom = ((CustomUserDetails) auth.getPrincipal()).getKingdom();
    return ResponseEntity.ok(buildingService.createBuilding(kingdom, dto));
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getBuildingById(@PathVariable Long id,
                                           Authentication auth)
    throws IdNotFoundException, ForbiddenActionException {

    //KingdomEntity kingdom = ((CustomUserDetails) auth.getPrincipal()).getKingdom();

    Long idd = ((CustomUserDetails) auth.getPrincipal()).getKingdom().getId();
    KingdomEntity kingdom = kingdomService.findByID(idd);

    return ResponseEntity.ok(buildingService.showActualBuildingDetails(kingdom, id));
  }
}