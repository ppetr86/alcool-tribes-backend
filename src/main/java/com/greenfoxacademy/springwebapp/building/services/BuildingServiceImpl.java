package com.greenfoxacademy.springwebapp.building.services;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingRequestDTO;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.building.repositories.BuildingRepository;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BuildingServiceImpl implements BuildingService {

  private static Environment env;
  private final BuildingRepository repo;

  @Override
  public BuildingEntity save(BuildingEntity entity) {
    return repo.save(entity);
  }

  @Override
  public BuildingEntity defineFinishedAt(BuildingEntity entity) {
    entity.setFinishedAt(entity.getStartedAt() + Long.parseLong(
            env.getProperty(String.format("building.%s.buildingTime", entity.getType()))));
    return entity;
  }

  @Override
  public boolean isBuildingTypeInRequestOk(BuildingRequestDTO dto) {
    for (BuildingType each : BuildingType.values()) {
      if (dto.getType().toLowerCase().equals(each.label))
        return true;
    }
    return false;
  }

  @Override
  public BuildingEntity setBuildingTypeOnEntity(String type) {
    BuildingEntity buildingEntity = new BuildingEntity();
    if (type.equalsIgnoreCase("townhall")) {
      buildingEntity.setType(BuildingType.TOWNHALL);
    } else if (type.equalsIgnoreCase("farm")) {
      buildingEntity.setType(BuildingType.FARM);
    } else if (type.equalsIgnoreCase("mine")) {
      buildingEntity.setType(BuildingType.MINE);
    } else if (type.equalsIgnoreCase("academy")) {
      buildingEntity.setType(BuildingType.ACADEMY);
    }
    return buildingEntity;
  }
}
