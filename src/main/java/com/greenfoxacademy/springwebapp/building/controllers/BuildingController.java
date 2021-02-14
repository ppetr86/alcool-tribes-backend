package com.greenfoxacademy.springwebapp.building.controllers;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingRequestDTO;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingsResponseDTO;
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.ErrorDTO;
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
  public ResponseEntity<BuildingsResponseDTO> getKingdomBuildings(Authentication auth) {
    KingdomEntity kingdom = ((CustomUserDetails) auth.getPrincipal()).getKingdom();

    List<BuildingEntity> list = buildingService.findBuildingsByKingdomId(kingdom.getId());
    return ResponseEntity.status(HttpStatus.OK).body(new BuildingsResponseDTO(list));
  }

  @PostMapping
  public ResponseEntity<?> buildBuilding(Authentication auth, @RequestBody @Valid BuildingRequestDTO dto)
      throws InvalidInputException, TownhallLevelException, NotEnoughResourceException, MissingParameterException {
    KingdomEntity kingdom = ((CustomUserDetails) auth.getPrincipal()).getKingdom();
    return ResponseEntity.ok(buildingService.createBuilding(kingdom, dto));
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> increaseTheGivenBuildingLevel(@PathVariable Long id,
                                                         Authentication auth){

    KingdomEntity kingdom = ((CustomUserDetails) auth.getPrincipal()).getKingdom();

    BuildingEntity actualBuilding = buildingService.findBuildingById(id);

    String result = buildingService.increaseTheGivenBuildingLevel(kingdom, actualBuilding);

    switch (result) {
      case "no id":
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("Id not found"));
      case "parameter missing":
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO("Missing parameter(s): <type>!"));
      case "town hall need higher level":
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ErrorDTO("Invalid building level || Cannot build buildings with higher level than the Townhall"));
      case "no resource":
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDTO("Not enough resource"));
      case "building details":
        BuildingEntity updatedBuilding = buildingService.updateBuilding(actualBuilding);
        return ResponseEntity.ok().body(updatedBuilding);
    }
    return null;
  }
}