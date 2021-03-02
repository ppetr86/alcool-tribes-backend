package com.greenfoxacademy.springwebapp.resource.services;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.resource.models.ResourceTimerTask;
import com.greenfoxacademy.springwebapp.common.services.TimeService;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
import com.greenfoxacademy.springwebapp.resource.models.dtos.ResourceListResponseDTO;
import com.greenfoxacademy.springwebapp.resource.models.dtos.ResourceResponseDTO;
import com.greenfoxacademy.springwebapp.resource.models.enums.ResourceType;
import com.greenfoxacademy.springwebapp.resource.repositories.ResourceRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class ResourceServiceImpl implements ResourceService {
  private ResourceRepository resourceRepository;
  private TimeService timeService;
  private Environment env;

  public boolean hasResourcesForTroop() {
    // TODO: has Resources For Troops creation
    return true;
  }

  @Override
  public boolean hasResourcesForBuilding() {
    // TODO: hasResourcesForBuilding
    return true;
  }

  @Override
  public List<ResourceEntity> createDefaultResources(KingdomEntity kingdomEntity) {
    return Arrays.stream(ResourceType.values())
        .map(type -> new ResourceEntity(kingdomEntity, type, 100, 10, timeService.getTime()))
        .collect(Collectors.toList());
  }

  @Override
  public ResourceListResponseDTO convertKingdomResourcesToListResponseDTO(KingdomEntity kingdom) {
    return ResourceListResponseDTO.builder()
        .withResources(kingdom.getResources().stream()
            .map(ResourceResponseDTO::new)
            .collect(Collectors.toList()))
        .build();
  }

  // TODO: when PUT kingdom/buildings/{buildingId} then also update resources
  @Override
  public ResourceEntity updateResourceGeneration(KingdomEntity kingdom, BuildingEntity building) {
    if (building.getType().equals(BuildingType.FARM) || building.getType().equals(BuildingType.MINE)) {
      ResourceEntity resource = doResourceUpdate(kingdom, building);
      if (resource != null) {
        log.info("Resource {} with ID {} will be updated. Actual amount is {}, actual generation is {}",
            resource.getType(), resource.getId(), resource.getAmount(), resource.getGeneration());
      } else {
        log.warn("Resource generation was not updated when creating new building of type {}!", building.getType());
      }
      return resource;
    }
    return null;
  }

  public ResourceEntity doResourceUpdate(KingdomEntity kingdom, BuildingEntity building) {
    ResourceEntity resourceToBeUpdated = findResourceByBuildingType(kingdom, building.getType());
    Integer newResourceGeneration = calculateNewResourceGeneration(resourceToBeUpdated, building);

    //sheduling the update to later time (when building is actually finished)
    int delay = timeService.getTimeBetween(timeService.getTime(), building.getFinishedAt()) * 1000;
    Timer timer = createNewTimer();
    ResourceTimerTask resourceTimerTask = new ResourceTimerTask(resourceToBeUpdated,
        newResourceGeneration, building) {
      @Override
      public void run() {
        ResourceEntity updatedResource = scheduledResourceUpdate(this.getResource(), this.getGeneration(),
            this.getBuilding());
      }
    };

    timer.schedule(resourceTimerTask, delay);

    return resourceToBeUpdated;
  }

  public Timer createNewTimer() {
    return new Timer();
  }

  public ResourceEntity scheduledResourceUpdate(ResourceEntity resourceToBeUpdated,
                                                Integer newResourceGeneration, BuildingEntity building) {
    //fetching most recent version of resource from DTB since resource could be updated in meantime
    ResourceEntity fetchedResource = resourceRepository.findById(resourceToBeUpdated.getId()).orElse(null);
    if (fetchedResource == null) {
      log.info("Respective resource was not found in database!");
    }

    Integer generatedResourcesInMeantime = calculateResourcesUntilBuildingIsFinished(building, fetchedResource);

    resourceToBeUpdated.setGeneration(newResourceGeneration);
    resourceToBeUpdated.setAmount(resourceToBeUpdated.getAmount() + generatedResourcesInMeantime);
    resourceToBeUpdated.setUpdatedAt(building.getFinishedAt());
    resourceRepository.save(resourceToBeUpdated);

    log.info("Resource {} with ID {} was updated. Actual amount is {}, actual generation is {}",
        resourceToBeUpdated.getType(), resourceToBeUpdated.getId(), resourceToBeUpdated.getAmount(),
        resourceToBeUpdated.getGeneration());

    return resourceToBeUpdated;
  }

  @Override
  public ResourceEntity findResourceByBuildingType(KingdomEntity kingdom, Enum buildingType) {
    ResourceEntity resource;
    if (buildingType.equals(BuildingType.FARM)) {
      resource = kingdom.getResources().stream()
          .filter(a -> a.getType().equals(ResourceType.FOOD))
          .findFirst()
          .orElse(null);
    } else if (buildingType.equals(BuildingType.MINE)) {
      resource = kingdom.getResources().stream()
          .filter(a -> a.getType().equals(ResourceType.GOLD))
          .findFirst()
          .orElse(null);
    } else {
      return null;
    }

    return resource;
  }

  public Integer calculateNewResourceGeneration(ResourceEntity resource, BuildingEntity building) {
    //distinguishing food/gold in case the values would differ in future
    Integer defaultFood = Integer.parseInt(env.getProperty("resourceEntity.food"));
    Integer defaultGold = Integer.parseInt(env.getProperty("resourceEntity.gold"));

    if (resource.getType().equals(ResourceType.FOOD)) {
      return resource.getGeneration() + building.getLevel() * defaultFood + defaultFood;
    }

    if (resource.getType().equals(ResourceType.GOLD)) {
      return resource.getGeneration() + building.getLevel() * defaultGold + defaultGold;
    }

    return null;
  }

  public Integer calculateResourcesUntilBuildingIsFinished(BuildingEntity building, ResourceEntity fetchedResource) {

    Long lastUpdateTime = fetchedResource.getUpdatedAt();
    Long finishedTime = building.getFinishedAt();
    int timeInSeconds = timeService.getTimeBetween(lastUpdateTime, finishedTime);

    //note: generated resources are calculated continuously, not based on predefined intervals (e.g. minute)
    double resourcesGenerated = (double) (timeInSeconds) / 60 * fetchedResource.getGeneration();
    return (int) resourcesGenerated;
  }

}
