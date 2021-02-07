package com.greenfoxacademy.springwebapp.building.services;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingRequestDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;

import java.util.List;
import java.util.Set;

public interface BuildingService {

  BuildingEntity save(BuildingEntity entity);

  BuildingEntity defineFinishedAt(BuildingEntity entity);

  boolean isBuildingTypeInRequestOk(BuildingRequestDTO dto);

  BuildingEntity setBuildingTypeOnEntity(String type);

  BuildingEntity createBuilding(KingdomEntity kingdom, BuildingRequestDTO dto);

  BuildingEntity defineHp(BuildingEntity entity);

  BuildingEntity findBuildingById(Long id);

  long countBuildings();

  boolean hasKingdomTownhall (KingdomEntity kingdom);

  List<BuildingEntity> findBuildingsByKingdomId(Long id);

  List<BuildingEntity> createDefaultBuildings(KingdomEntity kingdom);
}
