package com.greenfoxacademy.springwebapp.building.controllers;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingRequestDTO;
<<<<<<< HEAD:src/main/java/com/greenfoxacademy/springwebapp/building/controllers/BuildingsController.java
=======
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingsResponseDTO;
>>>>>>> ALTB-15-Petr:src/main/java/com/greenfoxacademy/springwebapp/building/controllers/BuildingController.java
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.ErrorDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.services.KingdomService;
import com.greenfoxacademy.springwebapp.resource.services.ResourceService;
import com.greenfoxacademy.springwebapp.security.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(BuildingController.URI)
public class BuildingController {

  public static final String URI = "/kingdom/buildings";

  private final BuildingService buildingService;
  private final KingdomService kingdomService;
  private final ResourceService resourceService;


  @GetMapping
  public ResponseEntity<BuildingsResponseDTO> getKingdomBuildings(Authentication auth) {
    KingdomEntity kingdom = ((CustomUserDetails) auth.getPrincipal()).getKingdom();

    List<BuildingEntity> list = buildingService.findBuildingsByKingdomId(kingdom.getId());
    return ResponseEntity.status(HttpStatus.OK).body(new BuildingsResponseDTO(list));
  }

  @PostMapping
<<<<<<< HEAD:src/main/java/com/greenfoxacademy/springwebapp/building/controllers/BuildingsController.java
  public ResponseEntity<?> buildBuilding(Principal principal,
=======
  public ResponseEntity<?> buildBuilding(Authentication auth,
>>>>>>> ALTB-15-Petr:src/main/java/com/greenfoxacademy/springwebapp/building/controllers/BuildingController.java
                                         @RequestBody @Valid BuildingRequestDTO dto, BindingResult bindingResult) {
    List<ObjectError> errorList = bindingResult.getAllErrors();

    KingdomEntity kingdom = ((CustomUserDetails) auth.getPrincipal()).getKingdom();
    if (!errorList.isEmpty()) {
      String error = errorList.get(0).getDefaultMessage();
      return ResponseEntity.badRequest().body(new ErrorDTO(error));
    } else if (dto.getType() == null || dto.getType().isEmpty()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO(
              "Missing parameter(s): type!"));
    } else if (!buildingService.isBuildingTypeInRequestOk(dto)) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ErrorDTO(
              "Invalid building type"));
    } else if (!kingdomService.hasKingdomTownhall()) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ErrorDTO(
              "Cannot build buildings with higher level than the Townhall"));
    } else if (resourceService.hasResourcesForBuilding()) {
      BuildingEntity building = buildingService.createBuilding(dto);
      return ResponseEntity.ok(building);
    } else {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDTO("Not enough resource"));
    }
  }
}