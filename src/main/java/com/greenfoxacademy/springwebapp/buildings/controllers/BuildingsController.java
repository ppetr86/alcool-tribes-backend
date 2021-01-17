package com.greenfoxacademy.springwebapp.buildings.controllers;

import com.greenfoxacademy.springwebapp.buildings.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.buildings.models.dtos.BuildingRequestDTO;
import com.greenfoxacademy.springwebapp.buildings.models.dtos.ErrorResponseDTO;
import com.greenfoxacademy.springwebapp.buildings.services.BuildingService;
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
  private BuildingService buildingService;
  private KingdomService kingdomService;

  @PostMapping("/kingdom/buildings")
  public ResponseEntity<?> buildBuilding(@RequestBody BuildingRequestDTO dto) {

    if (!hasText(dto.getType().toLowerCase())) {
      return ResponseEntity.badRequest().body(new ErrorResponseDTO("error", "Missing parameter(s): type!"));
    } else if (!buildingService.isBuildingTypeInRequestOk(dto) ||
            !kingdomService.hasKingdomTownhall()) {
      return new ResponseEntity<>(
              new ErrorResponseDTO("error", "Invalid building type || Cannot build buildings with higher level than the Townhall"),
              HttpStatus.NOT_ACCEPTABLE);
    } else if (kingdomService.hasResourcesForBuilding()) {
      BuildingEntity building = buildingService.createBuildingType(dto.getType());
      buildingService.setStartedAt(building);
      buildingService.defineFinishedAt(building);
      buildingService.save(building);
      return ResponseEntity.ok(building);
    } else {
      return new ResponseEntity<>(new ErrorResponseDTO("error", "Not enough resource"),
              HttpStatus.CONFLICT);
    }
  }
}
