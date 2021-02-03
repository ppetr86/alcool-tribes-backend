package com.greenfoxacademy.springwebapp.building.controllers;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingRequestDTO;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingsResponseDTO;
import com.greenfoxacademy.springwebapp.building.models.dtos.ErrorResponseDTO;
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.kingdom.services.KingdomService;
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
public class BuildingsController {

  public static final String URI_POST = "/kingdom/buildings";
  public static final String URI_GET = "/kingdom/{id}/buildings";

  private final BuildingService buildingService;
  private final KingdomService kingdmService;
  private final ResourceService resourceService;

  
  @GetMapping(URI_GET)
  public ResponseEntity<BuildingsResponseDTO> getKingdomBuildings(@PathVariable Long id) {

    List<BuildingEntity> list = buildingService.findBuildingsByKingdomId(id);
    return ResponseEntity.status(HttpStatus.OK).body(new BuildingsResponseDTO(list));
  }

  @PostMapping(URI_POST)
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
}