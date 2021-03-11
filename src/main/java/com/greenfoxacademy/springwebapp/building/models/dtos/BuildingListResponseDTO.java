package com.greenfoxacademy.springwebapp.building.models.dtos;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import lombok.Data;

import java.util.List;

@Data
public class BuildingListResponseDTO {

  private List<BuildingEntity> buildings;

  public BuildingListResponseDTO(List<BuildingEntity> buildings) {
    this.buildings = buildings;
  }
}
