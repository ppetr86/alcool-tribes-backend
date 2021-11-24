package com.greenfoxacademy.springwebapp.building.services;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingDetailsDTO;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingLevelDTO;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingRequestDTO;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.ForbiddenActionException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.IdNotFoundException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.InvalidInputException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.MissingParameterException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.NotEnoughResourceException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.TownhallLevelException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import java.util.List;

public interface BuildingService {

    List<BuildingDetailsDTO> getBuildingsByRequestParams(Integer level, Integer hp, Long startedAt);


    List<BuildingDetailsDTO> buildingsByGenericSpecification(Long id, Integer level,
                                                             Integer type, Long startedAt);


    List<BuildingDetailsDTO> buildingsByGenericSpecificationPaged(Long id, int pageNr, int pageSize);


    List<BuildingDetailsDTO> buildingsByGenericSpecificationSorted(Long id, String sort);


    BuildingEntity checkBuildingDetails(KingdomEntity kingdom, Long id, BuildingLevelDTO levelDTO)
            throws IdNotFoundException, MissingParameterException, TownhallLevelException, NotEnoughResourceException;


    BuildingEntity createBuilding(KingdomEntity kingdom, BuildingRequestDTO dto)
            throws InvalidInputException, TownhallLevelException, NotEnoughResourceException;


    List<BuildingEntity> createDefaultBuildings(KingdomEntity kingdom);


    BuildingEntity defineFinishedAt(BuildingEntity entity);


    BuildingEntity defineHp(BuildingEntity entity);


    List<Long> findAllIdsPaged(int size);


    BuildingEntity findBuildingById(Long id);


    BuildingEntity findBuildingWithHighestLevel(KingdomEntity kingdom, BuildingType townhall);


    List<BuildingEntity> findBuildingsByKingdomId(Long id);


    List<BuildingDetailsDTO> getAllBuildingsByType(int buildingType);


    List<BuildingDetailsDTO> getAllBuildingsByTypeLevelKingdomId(Integer type, int level);


    boolean hasKingdomTownhall(KingdomEntity kingdom);


    boolean isBuildingTypeInRequestOk(BuildingRequestDTO dto);


    BuildingEntity save(BuildingEntity entity);


    BuildingEntity setBuildingTypeOnEntity(String type);


    BuildingDetailsDTO showBuilding(KingdomEntity kingdomEntity, Long id)
            throws IdNotFoundException, ForbiddenActionException;


    BuildingEntity updateBuilding(KingdomEntity kingdom, Long id, BuildingLevelDTO levelDTO)
            throws IdNotFoundException, MissingParameterException, TownhallLevelException, NotEnoughResourceException;
}
