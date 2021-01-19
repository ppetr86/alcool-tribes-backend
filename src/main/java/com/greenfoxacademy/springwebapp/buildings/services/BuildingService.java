package com.greenfoxacademy.springwebapp.buildings.services;

import com.greenfoxacademy.springwebapp.buildings.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.buildings.models.dtos.BuildingRequestDTO;

public interface BuildingService {

  void save(BuildingEntity entity);

  void defineFinishedAt(BuildingEntity entity);

  boolean isBuildingTypeInRequestOk(BuildingRequestDTO dto);

  BuildingEntity createBuildingType(String type);

  void setStartedAt(BuildingEntity building);
}
