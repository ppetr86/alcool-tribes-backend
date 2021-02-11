package com.greenfoxacademy.springwebapp.troop.services;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.common.services.TimeService;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.ForbiddenCustomException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.InvalidAcademyIdException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.NotEnoughResourceException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.resource.services.ResourceService;
import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopRequestDTO;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopResponseDTO;
import com.greenfoxacademy.springwebapp.troop.repositories.TroopRepository;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TroopServiceImpl implements TroopService {
  ResourceService resourceService;
  TimeService timeService;
  TroopRepository troopRepository;
  private final Environment env;

  @Override
  public TroopResponseDTO createTroop(KingdomEntity kingdom, TroopRequestDTO requestDTO) throws
      ForbiddenCustomException, InvalidAcademyIdException, NotEnoughResourceException {

    BuildingEntity academy = kingdom.getBuildings().stream()
        .filter(building -> building.getId() == requestDTO.getBuildingId())
        .findFirst()
        .orElse(null);

    if(academy == null) throw new ForbiddenCustomException();

    if (!academy.getType().equals(BuildingType.ACADEMY)) throw new InvalidAcademyIdException();

    if (!resourceService.hasResourcesForTroop()) throw new NotEnoughResourceException();
    // TODO: after resources are defined, adjust logic for getting resources and their substracting when new troops are created.

    Integer troopLevel = academy.getLevel();
    Integer hp = troopLevel*getAppPropertyAsInt("troop.hp");
    Integer attack = troopLevel*getAppPropertyAsInt("troop.attack");
    Integer defence = troopLevel*getAppPropertyAsInt("troop.defence");
    Long startedAt = timeService.getTime();
    Long finishedAt = timeService.getTimeAfter(troopLevel*getAppPropertyAsInt("troop.buildingTime"));

    TroopEntity troop = troopRepository.save(
        new TroopEntity(troopLevel, hp, attack, defence, startedAt, finishedAt, kingdom));

    return new TroopResponseDTO(
        troop.getId(), troop.getLevel(), troop.getHp(), troop.getAttack(),
        troop.getDefence(), troop.getStartedAt(), troop.getFinishedAt());
  }

  private Integer getAppPropertyAsInt (String propertyName) {
    return Integer.parseInt(env.getProperty(propertyName));
  }
}
