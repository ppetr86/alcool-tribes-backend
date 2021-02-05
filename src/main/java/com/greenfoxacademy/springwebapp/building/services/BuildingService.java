package com.greenfoxacademy.springwebapp.building.services;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingRequestDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;

import java.util.Set;

public interface BuildingService {

  BuildingEntity save(BuildingEntity entity);

  BuildingEntity defineFinishedAt(BuildingEntity entity);

  boolean isBuildingTypeInRequestOk(BuildingRequestDTO dto);

  BuildingEntity setBuildingTypeOnEntity(String type);

  BuildingEntity createBuilding(BuildingRequestDTO dto);

  BuildingEntity defineHp(BuildingEntity entity);

  BuildingEntity findBuildingById(Long id);

  long countBuildings();

  Set<BuildingEntity> createDefaultBuildings(KingdomEntity kingdom);

}
