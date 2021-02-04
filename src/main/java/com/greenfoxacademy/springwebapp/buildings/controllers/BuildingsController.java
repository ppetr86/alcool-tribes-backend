package com.greenfoxacademy.springwebapp.buildings.controllers;

import com.greenfoxacademy.springwebapp.buildings.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.buildings.models.dtos.BuildingRequestDTO;
import com.greenfoxacademy.springwebapp.buildings.models.dtos.ErrorResponseDTO;
import com.greenfoxacademy.springwebapp.buildings.services.BuildingService;
import com.greenfoxacademy.springwebapp.common.services.TimeService;
import com.greenfoxacademy.springwebapp.kingdom.services.KingdomService;
import com.greenfoxacademy.springwebapp.player.models.dtos.ErrorDTO;
import com.greenfoxacademy.springwebapp.resource.services.ResourceService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(BuildingsController.URI)
public class BuildingsController {

  public static final String URI = "/kingdom/buildings";

  private final BuildingService buildingService;
  private final KingdomService kingdomService;
  private final TimeService timeService;
  private final ResourceService resourceService;

  @PostMapping
  public ResponseEntity<?> buildBuilding(
    @RequestBody @Valid BuildingRequestDTO dto, BindingResult bindingResult) {
    List<ObjectError> errorList = bindingResult.getAllErrors();

    if (!errorList.isEmpty()) {
      String error = errorList.get(0).getDefaultMessage();
      return ResponseEntity.badRequest().body(new ErrorResponseDTO(error));
    } else if (!buildingService.isBuildingTypeInRequestOk(dto)) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ErrorResponseDTO(
        "Invalid building type"));
    } else if (!kingdomService.hasKingdomTownhall()) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ErrorResponseDTO(
        "Cannot build buildings with higher level than the Townhall"));
    }
    if (resourceService.hasResourcesForBuilding()) {
      BuildingEntity building = buildingService.createBuilding(dto);
      return ResponseEntity.ok(building);
    } else {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponseDTO("Not enough resource"));
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getBuildingById(@PathVariable Long id) {
    BuildingEntity actualBuilding = buildingService.findBuildingById(id);

    if (actualBuilding == null) {
      if (id <= buildingService.countBuildings() && 0 < id) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDTO("Forbidden action"));
      } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("Id not found"));
      }
    } else {
      return ResponseEntity.ok().body(actualBuilding);
    }
  }
}
