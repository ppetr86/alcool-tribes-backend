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
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.util.StringUtils.hasText;

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
    } else if (!buildingService.isBuildingTypeInRequestOk(dto) ||
            !kingdomService.hasKingdomTownhall()) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ErrorResponseDTO(
              "Invalid building type || Cannot build buildings with higher level than the Townhall"));
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
