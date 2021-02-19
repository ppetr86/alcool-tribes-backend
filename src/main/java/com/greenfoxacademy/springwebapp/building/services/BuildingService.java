package com.greenfoxacademy.springwebapp.building.services;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingDetailsDTO;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingRequestDTO;

import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingSingleResponseDTO;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.*;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;

import java.util.List;

public interface BuildingService {

  BuildingEntity save(BuildingEntity entity);

  BuildingEntity defineFinishedAt(BuildingEntity entity);

  boolean isBuildingTypeInRequestOk(BuildingRequestDTO dto);

  BuildingEntity setBuildingTypeOnEntity(String type);

  BuildingEntity createBuilding(KingdomEntity kingdom, BuildingRequestDTO dto)
    throws InvalidInputException, TownhallLevelException, NotEnoughResourceException, MissingParameterException;;

  BuildingEntity defineHp(BuildingEntity entity);

  BuildingEntity findBuildingById(Long id);

  boolean kingdomHasThisBuilding(KingdomEntity kingdomEntity, BuildingEntity buildingEntity);

  BuildingDetailsDTO showActualBuildingDetails(KingdomEntity kingdomEntity, Long id)
    throws IdNotFoundException, ForbiddenActionException;

  boolean hasKingdomTownhall(KingdomEntity kingdom);

  List<BuildingEntity> findBuildingsByKingdomId(Long id);

  List<BuildingEntity> createDefaultBuildings(KingdomEntity kingdom);
}
