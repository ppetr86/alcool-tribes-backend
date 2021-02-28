package com.greenfoxacademy.springwebapp.troop.services;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.common.services.TimeService;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.ForbiddenActionException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.IdNotFoundException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.InvalidAcademyIdException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.InvalidBuildingTypeException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.InvalidInputException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.MissingParameterException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.NotEnoughResourceException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.resource.services.ResourceService;
import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopEntityResponseDTO;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopListResponseDto;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopRequestDTO;
import com.greenfoxacademy.springwebapp.troop.repositories.TroopRepository;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TroopServiceImpl implements TroopService {

  private ResourceService resourceService;
  private TimeService timeService;
  private TroopRepository troopRepository;
  private final Environment env;
  private BuildingService buildingService;

  @Override
  public TroopListResponseDto troopsToListDTO(KingdomEntity entity) {
    return new TroopListResponseDto(
        entity.getTroops().stream()
            .map(TroopEntityResponseDTO::new)
            .collect(Collectors.toList())
    );
  }

  @Override
  public TroopEntityResponseDTO createTroop(KingdomEntity kingdom, TroopRequestDTO requestDTO) throws
      ForbiddenActionException, InvalidAcademyIdException, NotEnoughResourceException {

    BuildingEntity academy = findAcademy(kingdom, requestDTO);

    if (academy == null) {
      throw new ForbiddenActionException();
    }

    if (!academy.getType().equals(BuildingType.ACADEMY)) {
      throw new InvalidAcademyIdException();
    }

    if (!resourceService.hasResourcesForTroop()) {
      throw new NotEnoughResourceException();
    }
    // TODO: after resources are defined, adjust logic for getting resources and their substracting when new troops are created.

    Integer troopLevel = academy.getLevel();
    Integer hp = troopLevel * getAppPropertyAsInt("troop.hp");
    Integer attack = troopLevel * getAppPropertyAsInt("troop.attack");
    Integer defence = troopLevel * getAppPropertyAsInt("troop.defence");
    Long startedAt = timeService.getTime();
    Long finishedAt = timeService.getTimeAfter(troopLevel * getAppPropertyAsInt("troop.buildingTime"));

    TroopEntity
        troop = new TroopEntity(troopLevel, hp, attack, defence, startedAt, finishedAt, kingdom);
    troopRepository.save(troop);

    return new TroopEntityResponseDTO(troop);
  }

  @Override
  public TroopEntityResponseDTO updateTroopLevel(KingdomEntity kingdomEntity, TroopRequestDTO requestDTO,
                                                 Long troopId) throws
      MissingParameterException, ForbiddenActionException, IdNotFoundException,
      InvalidBuildingTypeException, NotEnoughResourceException {

    BuildingEntity academy = findAcademy(kingdomEntity, requestDTO);

    if (requestDTO.getBuildingId() == null || requestDTO.getBuildingId() == 0
        || requestDTO.getBuildingId().toString().isEmpty()) {
      throw new MissingParameterException("buildingId");
    } else if (academy == null) {
      BuildingEntity actualBuilding = buildingService.findBuildingById(requestDTO.getBuildingId());
      if (actualBuilding == null) {
        throw new IdNotFoundException();
      } else {
        throw new ForbiddenActionException();
      }
    } else if (!academy.getType().equals(BuildingType.ACADEMY)) {
      throw new InvalidAcademyIdException();
    } else if (!resourceService.hasResourcesForTroop()) {
      throw new NotEnoughResourceException();
    } else if (!kingdomEntity.getId().equals(troopRepository.findKingdomIdByTroopId(troopId))) {
      throw new InvalidInputException("troop id!");
      //throwing invalid troop id in case of wrong id is provided. Not in swagger but i think it's important.
    }

    TroopEntity troopEntity = updateTroop(academy, troopId);
    troopRepository.save(troopEntity);
    return new TroopEntityResponseDTO(troopEntity);
  }

  private TroopEntity updateTroop(BuildingEntity academy, Long troopId) {
    Integer troopLevel = academy.getLevel();
    Long startedAt = timeService.getTime();
    Long finishedAt = timeService.getTimeAfter(troopLevel * getAppPropertyAsInt("troop.buildingTime"));

    TroopEntity troopEntity = troopRepository.findTroopEntityById(troopId);
    troopEntity.setLevel(troopLevel);
    troopEntity.setStartedAt(startedAt);
    troopEntity.setFinishedAt(finishedAt);

    return troopEntity;
  }

  private Integer getAppPropertyAsInt(String propertyName) {
    return Integer.parseInt(env.getProperty(propertyName));
  }

  private BuildingEntity findAcademy(KingdomEntity kingdomEntity, TroopRequestDTO requestDTO) {
    return kingdomEntity.getBuildings().stream()
        .filter(building -> building.getId().equals(requestDTO.getBuildingId()))
        .findFirst()
        .orElse(null);
  }

  @Override
  public TroopEntityResponseDTO getTroop(KingdomEntity kingdom, Long troopId)
      throws ForbiddenActionException, IdNotFoundException {

    TroopEntity myTroop = kingdom.getTroops().stream()
        .filter(a -> a.getId().equals(troopId))
        .findFirst()
        .orElse(null);

    if (myTroop == null) {
      TroopEntity existingTroop = findTroopById(troopId);
      if (existingTroop == null) {
        throw new IdNotFoundException();
      } else {
        throw new ForbiddenActionException();
      }
    }

    return new TroopEntityResponseDTO(myTroop);
  }

  @Override
  public TroopEntity findTroopById(Long id) {
    return troopRepository.findById(id).orElse(null);
  }
}