package com.greenfoxacademy.springwebapp.building.models.dtos;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import java.util.List;
import lombok.Data;

@Data
public class BuildingsResponseDTO {

  private List<BuildingEntity> buildings;

  public BuildingsResponseDTO(List<BuildingEntity> buildings) {
    this.buildings = buildings;
  }
}
