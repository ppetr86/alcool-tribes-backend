package com.greenfoxacademy.springwebapp.building.models.dtos;

//TODO: ALTB-15
import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BuildingsInKingdomResponseDTO {

  private List<BuildingEntity> buildings;

  public BuildingsInKingdomResponseDTO(List<BuildingEntity> buildings) {
    this.buildings = buildings;
  }
}
