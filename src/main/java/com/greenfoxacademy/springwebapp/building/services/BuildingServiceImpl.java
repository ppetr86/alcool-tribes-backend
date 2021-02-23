package com.greenfoxacademy.springwebapp.building.services;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingLevelDTO;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingRequestDTO;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.building.repositories.BuildingRepository;
import com.greenfoxacademy.springwebapp.common.services.TimeService;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.*;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.resource.services.ResourceService;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    String time = env.getProperty(String.format("building.%s.buildingTime", entity.getType().buildingType.toLowerCase()));
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
  public String checkBuildingDetails(KingdomEntity kingdom, Long id, BuildingLevelDTO request)
    throws IdNotFoundException, MissingParameterException, TownhallLevelException, NotEnoughResourceException {

    BuildingEntity townHall = kingdom.getBuildings().stream()
      .filter(building -> building.getType().equals(BuildingType.TOWNHALL))
      .findFirst()
      .get();

    BuildingEntity building = findBuildingById(id);

    if (building == null) {
      throw new IdNotFoundException();
    } else if (request == null || request.getLevel() == 0) {
      throw new MissingParameterException("level");
    } else if (!resourceService.hasResourcesForBuilding()) {
      throw new NotEnoughResourceException();
    } else if (building.getType() == townHall.getType()) {
      return "townhall";
    } else if (townHall.getLevel() < request.getLevel()) {
      throw new TownhallLevelException();
    } else {
      return "building details";
    }
  }

  @Override
  public BuildingEntity updateBuilding(Long id, BuildingLevelDTO levelDTO) {
    BuildingEntity building = findBuildingById(id);

    if (building.getType().equals(BuildingType.TOWNHALL)) {
      building.setLevel(levelDTO.getLevel());
      building.setHp(levelDTO.getLevel() * 200);
      building.setStartedAt(timeService.getTime());
      building.setFinishedAt(building.getStartedAt() + (levelDTO.getLevel() * 120));
      return repo.save(building);
    } else if (building.getType().equals(BuildingType.ACADEMY)) {
      building.setLevel(levelDTO.getLevel());
      building.setHp(levelDTO.getLevel() * 150);
      building.setStartedAt(timeService.getTime());
      building.setFinishedAt(building.getStartedAt() + (levelDTO.getLevel() * 90));
      return repo.save(building);
    } else {
      building.setLevel(levelDTO.getLevel());
      building.setHp(levelDTO.getLevel() * 100);
      building.setStartedAt(timeService.getTime());
      building.setFinishedAt(building.getStartedAt() + (levelDTO.getLevel() * 60));
      return repo.save(building);
    }
  }

  @Override
  public List<BuildingEntity> findBuildingsByKingdomId(Long id) {
    return repo.findAllByKingdomId(id);
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
    throws InvalidInputException, TownhallLevelException, NotEnoughResourceException, MissingParameterException {
    if (dto.getType().trim().isEmpty()) throw new MissingParameterException("type");
    if (!isBuildingTypeInRequestOk(dto)) throw new InvalidInputException("building type");
    if (!hasKingdomTownhall(kingdom)) throw new TownhallLevelException();
    if (!resourceService.hasResourcesForBuilding()) throw new NotEnoughResourceException();
    BuildingEntity result = setBuildingTypeOnEntity(dto.getType());
    result.setStartedAt(timeService.getTime());
    result = defineFinishedAt(result);
    result = defineHp(result);
    result = save(result);
    return result;
  }

  @Override
  public List<BuildingEntity> createDefaultBuildings(KingdomEntity kingdom) {
    return Arrays.stream(BuildingType.values())
      .map(type -> new BuildingEntity(kingdom, type, 1))
      .collect(Collectors.toList());
  }

  @Override
  public boolean hasKingdomTownhall(KingdomEntity kingdom) {
    if (kingdom.getBuildings() == null) return false;
    return kingdom.getBuildings().stream()
      .anyMatch(building -> building.getType().equals(BuildingType.TOWNHALL));
  }

}
