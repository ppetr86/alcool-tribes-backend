package com.greenfoxacademy.springwebapp.resource.services;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.common.services.TimeService;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
import com.greenfoxacademy.springwebapp.resource.models.dtos.ResourceListResponseDTO;
import com.greenfoxacademy.springwebapp.resource.models.dtos.ResourceResponseDTO;
import com.greenfoxacademy.springwebapp.resource.models.enums.ResourceType;
import com.greenfoxacademy.springwebapp.resource.repositories.ResourceRepository;
import java.util.Timer;
import java.util.TimerTask;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

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
    return false;
  }

  @Override
  public ResourceListResponseDTO convertKingdomResourcesToListResponseDTO(KingdomEntity kingdom) {
    return ResourceListResponseDTO.builder()
        .withResources(kingdom.getResources().stream()
            .map(ResourceResponseDTO::new)
            .collect(Collectors.toList()))
        .build();
  }

  @Override
  public ResourceEntity updateResourceGeneration(KingdomEntity kingdom, BuildingEntity building) {
    ResourceEntity resourceToBeUpdated = findResourceBasedOnBuildingType(kingdom,building.getType());

    Integer newResourceGeneration = calculateNewResourceGeneration(resourceToBeUpdated, building);

    //updating selected resource properties in Database in later time when building is finished
    int delay = timeService.getTimeBetween(building.getFinishedAt(),timeService.getTime())*1000;
    Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        //fetching most recent version of resource from DTB since resource could be updated before my method is actually run
        ResourceEntity fetchedResource = resourceRepository.findById(resourceToBeUpdated.getId()).orElse(null);
        if (fetchedResource != null) {
          log.info("Fetched resource ID: {} , type: {}", fetchedResource.getId(), fetchedResource.getType());
        } else log.info("Respective resource was not found in database!");

        //calculating resources which were generated since last update of resource until new building is done/upgraded
        Integer generatedResourcesInMeantime = calculateResourcesUntilBuildingIsFinished(building, fetchedResource);

        resourceToBeUpdated.setGeneration(newResourceGeneration);
        resourceToBeUpdated.setAmount(resourceToBeUpdated.getAmount()+generatedResourcesInMeantime);
        resourceToBeUpdated.setUpdatedAt(building.getFinishedAt());
        resourceRepository.save(resourceToBeUpdated);
      }
    }, delay);
    return resourceToBeUpdated;
  }

  @Override
  public ResourceEntity findResourceBasedOnBuildingType(KingdomEntity kingdom, Enum buildingType) {
    ResourceEntity resource;
    if(buildingType.equals(BuildingType.FARM)) {
      resource = kingdom.getResources().stream()
          .filter(a -> a.getType().equals(ResourceType.FOOD))
          .findFirst()
          .orElse(null);
    } else if (buildingType.equals(BuildingType.MINE)) {
      resource = kingdom.getResources().stream()
          .filter(a -> a.getType().equals(ResourceType.GOLD))
          .findFirst()
          .orElse(null);
    } else return null;

    return resource;
  }

  public Integer calculateNewResourceGeneration(ResourceEntity resource, BuildingEntity building) {
    //distinguishing food/gold in case the values would differ in future
    Integer defaultFood = Integer.parseInt(env.getProperty("resourceEntity.food"));
    Integer defaultGold = Integer.parseInt(env.getProperty("resourceEntity.gold"));

    if (resource.getType().equals(ResourceType.FOOD)) {
      return resource.getGeneration() + building.getLevel()*defaultFood + defaultFood;
    }

    if (resource.getType().equals(ResourceType.GOLD)) {
      return resource.getGeneration() + building.getLevel()*defaultGold + defaultGold;
    }

    return null;
  }

  public Integer calculateResourcesUntilBuildingIsFinished(BuildingEntity building, ResourceEntity fetchedResource) {

    Long lastUpdateTime = fetchedResource.getUpdatedAt();
    Long finishedTime = building.getFinishedAt();
    int timeInSeconds = timeService.getTimeBetween(finishedTime, lastUpdateTime);

    //generated resources are calculated continuously, not based on predefined intervals (e.g. minute)
    double resourcesGenerated = (double)(timeInSeconds)/60*fetchedResource.getGeneration();
    return (int)resourcesGenerated;
  }

}
