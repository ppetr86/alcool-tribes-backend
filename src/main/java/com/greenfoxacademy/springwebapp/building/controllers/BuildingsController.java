package com.greenfoxacademy.springwebapp.building.controllers;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingRequestDTO;
import com.greenfoxacademy.springwebapp.building.models.dtos.ErrorResponseDTO;
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.commonServices.TimeService;
import com.greenfoxacademy.springwebapp.kingdom.services.KingdomService;
import com.greenfoxacademy.springwebapp.resource.services.ResourceService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.util.StringUtils.hasText;

@RestController
@AllArgsConstructor
public class BuildingsController {
  private final BuildingService buildingService;
  private final KingdomService kingdomService;
  private final TimeService timeService;
  private final ResourceService resourceService;

  @PostMapping("/kingdom/buildings")
  public ResponseEntity<?> buildBuilding(@RequestBody BuildingRequestDTO dto) {

    if (!hasText(dto.getType().toLowerCase())) {
      return ResponseEntity.badRequest().body(new ErrorResponseDTO("Missing parameter(s): type!"));
    } else if (!buildingService.isBuildingTypeInRequestOk(dto) ||
            !kingdomService.hasKingdomTownhall()) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ErrorResponseDTO("Invalid building type || Cannot build buildings with higher level than the Townhall"));
    } else if (resourceService.hasResourcesForBuilding()) {
      BuildingEntity building = buildingService.setBuildingTypeOnEntity(dto.getType());
      building.setStartedAt(timeService.getTime());
      buildingService.defineFinishedAt(building);
      buildingService.save(building);
      return ResponseEntity.ok(building);
    } else {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponseDTO("Not enough resource"));
    }
  }
}
