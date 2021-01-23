package com.greenfoxacademy.springwebapp.building.services;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingRequestDTO;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.building.repositories.BuildingRepository;
import com.greenfoxacademy.springwebapp.commonServices.TimeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BuildingServiceImpl implements BuildingService {

  //private static Environment env;
  private final BuildingRepository repo;
  private final TimeService timeService;

  @Override
  public BuildingEntity save(BuildingEntity entity) {
    return repo.save(entity);
  }

  /* Kond code which does not work, or I dont know how to make it work.
 @Override
  public BuildingEntity defineFinishedAt(BuildingEntity entity) {
    entity.setFinishedAt(entity.getStartedAt() + Long.parseLong(
            env.getProperty(String.format("building.%s.buildingTime", entity.getType().label))));
    return entity;
  }*/

  @Override
  public BuildingEntity defineFinishedAt(BuildingEntity entity) {
    for (BuildingType each : BuildingType.values()) {
      if (each.equals(entity.getType()))
        entity.setFinishedAt(entity.getStartedAt() + each.buildTime);
    }
    return entity;
  }

  @Override
  public boolean isBuildingTypeInRequestOk(BuildingRequestDTO dto) {
    for (BuildingType each : BuildingType.values()) {
      if (dto.getType().toLowerCase().equals(each.buildingType))
        return true;
    }
    return false;
  }

  @Override
  public BuildingEntity setBuildingTypeOnEntity(String type) {
    BuildingEntity building = new BuildingEntity();
    for (BuildingType each : BuildingType.values()) {
      if (each.buildingType.equalsIgnoreCase(type))
        building.setType(each);
    }
    return building;
  }

  @Override
  public BuildingEntity createBuilding(BuildingRequestDTO dto) {
    BuildingEntity result = setBuildingTypeOnEntity(dto.getType());
    result.setStartedAt(timeService.getTime());
    result = defineFinishedAt(result);
    result = save(result);
    return result;
  }
}
