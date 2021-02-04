package com.greenfoxacademy.springwebapp.building.controllers;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingRequestDTO;
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.ErrorDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.services.KingdomService;
import com.greenfoxacademy.springwebapp.resource.services.ResourceService;
import com.greenfoxacademy.springwebapp.security.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(BuildingsController.URI)
public class BuildingsController {

  public static final String URI = "/kingdom/buildings";

  private final BuildingService buildingService;
  private final KingdomService kingdomService;
  private final ResourceService resourceService;

  @PostMapping
  public ResponseEntity<?> buildBuilding(Principal principal,
                                         @RequestBody @Valid BuildingRequestDTO dto, BindingResult bindingResult) {
    List<ObjectError> errorList = bindingResult.getAllErrors();

    KingdomEntity kingdom = ((CustomUserDetails) ((UsernamePasswordAuthenticationToken) principal).getPrincipal()).getKingdom();
    if (!errorList.isEmpty()) {
      String error = errorList.get(0).getDefaultMessage();
      return ResponseEntity.badRequest().body(new ErrorDTO(error));
    } else if (!buildingService.isBuildingTypeInRequestOk(dto)) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ErrorDTO(
              "Invalid building type"));
    } else if (!kingdomService.hasKingdomTownhall()) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ErrorDTO(
              "Cannot build buildings with higher level than the Townhall"));
    }
    if (resourceService.hasResourcesForBuilding()) {
      BuildingEntity building = buildingService.createBuilding(dto);
      return ResponseEntity.ok(building);
    } else {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDTO("Not enough resource"));
    }
  }
}