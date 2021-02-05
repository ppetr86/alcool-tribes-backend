package com.greenfoxacademy.springwebapp.building.models.dtos;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class BuildingsResponseDTO {

  private List<BuildingEntity> buildings;

  public BuildingsResponseDTO(List<BuildingEntity> buildings) {
    this.buildings = buildings;
  }
}
