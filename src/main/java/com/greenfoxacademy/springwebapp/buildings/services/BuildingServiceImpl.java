package com.greenfoxacademy.springwebapp.buildings.services;

import com.greenfoxacademy.springwebapp.buildings.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.buildings.models.dtos.BuildingRequestDTO;
import com.greenfoxacademy.springwebapp.buildings.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.buildings.repositories.BuildingRepository;
import com.greenfoxacademy.springwebapp.common.services.TimeService;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BuildingServiceImpl implements BuildingService {

  private final Environment env;
  private final BuildingRepository repo;
  private final TimeService timeService;

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
  public List<BuildingEntity> findBuildingsByKingdomId(Long id) {
    return repo.findBuildingsByKingdomID(id);
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
  public BuildingEntity createBuilding(BuildingRequestDTO dto) {
    BuildingEntity result = setBuildingTypeOnEntity(dto.getType());
    result.setStartedAt(timeService.getTime());
    result = defineFinishedAt(result);
    result = defineHp(result);
    result = save(result);
    return result;
  }
}