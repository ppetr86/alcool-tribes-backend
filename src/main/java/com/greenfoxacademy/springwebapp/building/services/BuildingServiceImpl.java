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

  private BuildingRepository repo;
  private TimeService timeService;

  @Override
  public void save(BuildingEntity entity) {
    repo.save(entity);
  }

  @Override
  public void defineFinishedAt(BuildingEntity entity) {
    if (entity.getType().label.equalsIgnoreCase("townhall"))
      entity.setFinishedAt(entity.getStartedAt() + 120);

    if (entity.getType().label.equalsIgnoreCase("farm") ||
            entity.getType().label.equalsIgnoreCase("mine"))
      entity.setFinishedAt(entity.getStartedAt() + 60);

    if (entity.getType().label.equals("academy"))
      entity.setFinishedAt(entity.getStartedAt() + 90);
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
  public BuildingEntity createBuildingType(String type) {
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

  @Override
  public void setStartedAt(BuildingEntity building) {

    building.setStartedAt(timeService.epochTimeNow());
  }
}
