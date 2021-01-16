package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.models.dtos.BuildingRequestDTO;
import com.greenfoxacademy.springwebapp.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.services.BuildingService;
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
public class KingdomBuildingsController {

  private TimeService timeService;
  private BuildingService buildingService;
  private KingdomService kingdomService;

  @PostMapping("/kingdom/buildings")
  public ResponseEntity<?> buildNewBuildingsPost(@RequestBody BuildingRequestDTO dto) {
    BuildingEntity building = new BuildingEntity();
    String type = dto.getType().toLowerCase();
    if (!hasText(type)) {
      //"status": "error",
      //  "message": "Missing parameter(s): type!"
      return ResponseEntity.badRequest().body();
    }

    if (!(type.equals("townhall") || type.equals("farm") ||type.equals("mine") ||type.equals("academy")) || !kingdomService.hasTownhall() ){
      //"status": "error",  //  "message": "Invalid building type || Cannot build buildings with higher level than the Townhall"
      return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    if (dto.getType().toLowerCase().equals("townhall") && kingdomService.remainingResource() >= 200) {
      building.setType(BuildingType.TOWNHALL);
    } else if (dto.getType().toLowerCase().equals("farm")  && kingdomService.remainingResource() >= 100) {

      building.setType(BuildingType.FARM);
    } else if (dto.getType().toLowerCase().equals("mine") && kingdomService.remainingResource() >= 100) {
      building.setType(BuildingType.MINE);
    } else if (dto.getType().toLowerCase().equals("academy") && kingdomService.remainingResource() >= 150) {
      building.setType(BuildingType.ACADEMY);
    } else {
        //"status": "error","message": "Not enough resource"
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }


    buildingService.defineFinishedAt(building);
    buildingService.save(building);
    return ResponseEntity.ok(building);
  }
}
