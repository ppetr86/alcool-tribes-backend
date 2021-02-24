package com.greenfoxacademy.springwebapp.building.services;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingRequestDTO;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.InvalidInputException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.NotEnoughResourceException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.TownhallLevelException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import java.util.List;

public interface BuildingService {

  BuildingEntity save(BuildingEntity entity);

  BuildingEntity defineFinishedAt(BuildingEntity entity);

  boolean isBuildingTypeInRequestOk(BuildingRequestDTO dto);

  BuildingEntity setBuildingTypeOnEntity(String type);

  BuildingEntity createBuilding(KingdomEntity kingdom, BuildingRequestDTO dto)
      throws InvalidInputException, TownhallLevelException, NotEnoughResourceException;

  BuildingEntity defineHp(BuildingEntity entity);

  List<BuildingEntity> findBuildingsByKingdomId(Long id);

  List<BuildingEntity> createDefaultBuildings(KingdomEntity kingdom);

  boolean hasKingdomTownhall(KingdomEntity kingdom);

}
