package com.greenfoxacademy.springwebapp.buildings.controllers;

import com.greenfoxacademy.springwebapp.buildings.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.buildings.models.dtos.BuildingRequestDTO;
import com.greenfoxacademy.springwebapp.buildings.services.BuildingService;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.InvalidBuildingTypeException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.MissingParameterException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.NotEnoughResourceException;
import com.greenfoxacademy.springwebapp.kingdoms.services.KingdomService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.util.StringUtils.hasText;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class BuildingsController {
  private final BuildingService buildingService;
  private final KingdomService kingdomService;

  @PostMapping("/kingdom/buildings")
  public ResponseEntity<?> buildBuilding(@RequestBody BuildingRequestDTO dto) throws MissingParameterException, InvalidBuildingTypeException, NotEnoughResourceException {

    if (!hasText(dto.getType().toLowerCase())) {
      throw new MissingParameterException();
    } else if (!buildingService.isBuildingTypeInRequestOk(dto) ||
            !kingdomService.hasKingdomTownhall()) {
      throw new InvalidBuildingTypeException();
    } else if (kingdomService.hasResourcesForBuilding()) {
      BuildingEntity building = buildingService.createBuildingType(dto.getType());
      buildingService.setStartedAt(building);
      buildingService.defineFinishedAt(building);
      buildingService.save(building);
      return ResponseEntity.ok(building);
    } else {
      throw new NotEnoughResourceException();
    }
  }
}
