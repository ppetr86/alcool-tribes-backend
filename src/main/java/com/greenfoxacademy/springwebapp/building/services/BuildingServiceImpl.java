package com.greenfoxacademy.springwebapp.building.services;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingRequestDTO;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.building.repositories.BuildingRepository;
import com.greenfoxacademy.springwebapp.common.services.TimeService;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.InvalidInputException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.MissingParameterException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.NotEnoughResourceException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.TownhallLevelException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
import com.greenfoxacademy.springwebapp.resource.services.ResourceService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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
    if (dto.getType().trim().isEmpty()) {
      throw new MissingParameterException("type");
    }
    if (!isBuildingTypeInRequestOk(dto)) {
      throw new InvalidInputException("building type");
    }
    if (!hasKingdomTownhall(kingdom)) {
      throw new TownhallLevelException();
    }
    if (!resourceService.hasResourcesForBuilding()) {
      throw new NotEnoughResourceException();
    }
    BuildingEntity result = setBuildingTypeOnEntity(dto.getType());
    result.setStartedAt(timeService.getTime());
    result = defineFinishedAt(result);
    result = defineHp(result);
    result.setLevel(1);
    result.setKingdom(kingdom);
    result = save(result);

    //updating Resource Generation
    if (result.getType().equals(BuildingType.FARM) || result.getType().equals(BuildingType.MINE)) {
      ResourceEntity resource = resourceService.updateResourceGeneration(kingdom, result);
      if (resource != null) {
        log.info("Resource {} with ID {} will be updated. Actual amount is {}, actual generation is {}",
            resource.getType(), resource.getId(), resource.getAmount(), resource.getGeneration());
      } else {
        log.warn("Resource generation was not updated when creating new building of type {}!", result.getType());
      }
    }

    return result;
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
