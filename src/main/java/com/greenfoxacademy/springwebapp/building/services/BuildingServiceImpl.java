package com.greenfoxacademy.springwebapp.building.services;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingDetailsDTO;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingLevelDTO;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingRequestDTO;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.building.repositories.BuildingRepository;
import com.greenfoxacademy.springwebapp.common.services.TimeService;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.ForbiddenActionException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.IdNotFoundException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.InvalidInputException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.MissingParameterException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.NotEnoughResourceException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.TownhallLevelException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.resource.services.ResourceService;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BuildingServiceImpl implements BuildingService {

  private final Environment env;
  private final BuildingRepository repo;
  private final TimeService timeService;
  private final ResourceService resourceService;

  @Override
  public BuildingEntity save(BuildingEntity entity) {
    return repo.save(entity);
  }

  @Override
  public BuildingEntity defineFinishedAt(BuildingEntity entity) {
    String time =
        env.getProperty(String.format("building.%s.buildingTime", entity.getType().buildingType.toLowerCase()));
    entity.setFinishedAt(entity.getStartedAt() + Long.parseLong(time));
    return entity;
  }

  @Override
  public BuildingEntity defineHp(BuildingEntity entity) {
    String hp = env.getProperty(String.format("building.%s.hp", entity.getType().buildingType.toLowerCase()));
    entity.setHp(Integer.parseInt(hp));
    return entity;
  }

  @Override
  public BuildingEntity findBuildingById(Long id) {
    return repo.findById(id).orElse(null);
  }

  @Override
  public BuildingEntity updateBuilding(KingdomEntity kingdom, Long id, BuildingLevelDTO levelDTO)
      throws IdNotFoundException, MissingParameterException, TownhallLevelException, NotEnoughResourceException {

    BuildingEntity building = checkBuildingDetails(kingdom, id, levelDTO);

    int buildingHp = Integer.parseInt(
        Objects.requireNonNull(
            env.getProperty(String.format("building.%s.hp", building.getType().buildingType.toLowerCase()))));

    int buildingTime = Integer.parseInt(
        Objects.requireNonNull(
            env.getProperty(String.format("building.%s.buildingTime", building.getType().buildingType.toLowerCase()))));

    building.setLevel(levelDTO.getLevel());
    building.setHp(levelDTO.getLevel() * buildingHp);
    building.setStartedAt(timeService.getTime());
    building.setFinishedAt(building.getStartedAt() + (levelDTO.getLevel() * buildingTime));
    return repo.save(building);
  }

  private BuildingEntity checkBuildingDetails(KingdomEntity kingdom, Long id, BuildingLevelDTO levelDTO)
      throws IdNotFoundException, MissingParameterException, TownhallLevelException, NotEnoughResourceException,
      ForbiddenActionException {
    BuildingEntity townHall = getTownHallFromKingdom(kingdom);
    BuildingEntity building = findBuildingById(id);

    if (building == null) {
      throw new IdNotFoundException();
    } else if (levelDTO == null || levelDTO.getLevel() == 0) {
      throw new MissingParameterException("level");
    } else if (!findBuildingsByKingdomId(kingdom.getId()).contains(building)) {
      throw new ForbiddenActionException();
    } else if (!resourceService.hasResourcesForBuilding()) {
      throw new NotEnoughResourceException();
    } else if (building.getType() == townHall.getType()) {
      return building;
    } else if (townHall.getLevel() < levelDTO.getLevel()) {
      throw new TownhallLevelException();
    } else {
      return building;
    }
  }

  private BuildingEntity getTownHallFromKingdom(KingdomEntity kingdom) {
    return kingdom.getBuildings().stream()
        .filter(building -> building.getType().equals(BuildingType.TOWNHALL))
        .findFirst()
        .get();
  }

  @Override
  public boolean isBuildingTypeInRequestOk(BuildingRequestDTO dto) {
    try {
      BuildingType.valueOf(dto.getType().toUpperCase());
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  @Override
  public BuildingEntity setBuildingTypeOnEntity(String type) {
    BuildingEntity building = new BuildingEntity();

    try {
      BuildingType.valueOf(type.toUpperCase());
      building.setType(BuildingType.valueOf(type.toUpperCase()));
      return building;
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

  @Override
  public BuildingEntity createBuilding(KingdomEntity kingdom, BuildingRequestDTO dto)
      throws InvalidInputException, TownhallLevelException, NotEnoughResourceException {

    if (!isBuildingTypeInRequestOk(dto)) throw new InvalidInputException("building type");
    if (!hasKingdomTownhall(kingdom)) throw new TownhallLevelException();
    if (!resourceService.hasResourcesForBuilding()) throw new NotEnoughResourceException();

    BuildingEntity result = setBuildingTypeOnEntity(dto.getType());
    result.setStartedAt(timeService.getTime());
    result.setKingdom(kingdom);
    result.setLevel(1);
    result = defineFinishedAt(result);
    result = defineHp(result);
    result = save(result);
    return result;
  }

  @Override
  public BuildingDetailsDTO showBuilding(KingdomEntity kingdom, Long id)
      throws IdNotFoundException, ForbiddenActionException {

    BuildingEntity myBuilding = kingdom.getBuildings().stream()
        .filter(b -> b.getId().equals(id))
        .findFirst()
        .orElse(null);

    if (myBuilding == null) {
      BuildingEntity actualBuilding = findBuildingById(id);
      if (actualBuilding == null) {
        throw new IdNotFoundException();
      } else {
        throw new ForbiddenActionException();
      }
    }
    return new BuildingDetailsDTO(myBuilding);
  }

  @Override
  public List<BuildingEntity> findBuildingsByKingdomId(Long id) {
    return repo.findAllByKingdomId(id);
  }

  @Override
  public List<BuildingEntity> createDefaultBuildings(KingdomEntity kingdom) {
    return Arrays.stream(BuildingType.values())
        .map(type -> new BuildingEntity(kingdom,
            type,
            1,
            Integer.parseInt(env.getProperty(String.format("building.%s.hp",
                type.toString().toLowerCase()))),
            timeService.getTime(),
            timeService.getTime()))
        .collect(Collectors.toList());
  }

  @Override
  public boolean hasKingdomTownhall(KingdomEntity kingdom) {
    if (kingdom.getBuildings() == null) {
      return false;
    }
    return kingdom.getBuildings().stream()
        .anyMatch(building -> building.getType().equals(BuildingType.TOWNHALL));
  }
}


