package com.greenfoxacademy.springwebapp.buildings.models.dtos;

import com.greenfoxacademy.springwebapp.buildings.models.BuildingEntity;
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
