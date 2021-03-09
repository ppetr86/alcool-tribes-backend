package com.greenfoxacademy.springwebapp.troop.services;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.common.services.TimeService;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.ForbiddenActionException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.IdNotFoundException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.InvalidAcademyIdException;
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
    BuildingEntity academy = kingdom.getBuildings().stream()
        .filter(building -> building.getId().equals(requestDTO.getBuildingId()))
        .findFirst()
        .orElse(null);

    if (academy == null) {
      throw new ForbiddenActionException();
    } else if (!academy.getType().equals(BuildingType.ACADEMY)) {
      throw new InvalidAcademyIdException();
    }
    int troopCosts = (int) academy.getLevel() * getAppPropertyAsInt("troop.buildingCosts");
    if (!resourceService.hasResourcesForTroop(kingdom, troopCosts)) {
      throw new NotEnoughResourceException();
    }
    Integer troopLevel = academy.getLevel();
    TroopEntity troop = buildTroopFromTroopProperties(kingdom, troopLevel);
    troop = troopRepository.save(troop);
    return new TroopEntityResponseDTO(troop);
  }

  private TroopEntity buildTroopFromTroopProperties(KingdomEntity kingdom, Integer troopLevel) {
    Integer hp = troopLevel * getAppPropertyAsInt("troop.hp");
    Integer attack = troopLevel * getAppPropertyAsInt("troop.attack");
    Integer defence = troopLevel * getAppPropertyAsInt("troop.defence");
    Long startedAt = timeService.getTime();
    Long finishedAt = timeService.getTimeAfter(troopLevel * getAppPropertyAsInt("troop.buildingTime"));
    return new TroopEntity(troopLevel, hp, attack, defence, startedAt, finishedAt, kingdom);
  }

  private Integer getAppPropertyAsInt(String propertyName) {
    return Integer.parseInt(env.getProperty(propertyName));
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