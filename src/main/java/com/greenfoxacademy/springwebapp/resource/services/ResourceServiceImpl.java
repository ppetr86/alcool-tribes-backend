package com.greenfoxacademy.springwebapp.resource.services;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.common.services.TimeService;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
import com.greenfoxacademy.springwebapp.resource.models.ResourceTimerTask;
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
import java.util.Objects;
import java.util.Timer;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ResourceServiceImpl implements ResourceService {
  private ResourceRepository resourceRepository;
  private TimeService timeService;
  private Environment env;

  @Override
  public boolean hasResourcesForTroop(KingdomEntity kingdom, int amountChange) {
    int actualAmount = calculateActualResource(kingdom, ResourceType.GOLD);
    return amountChange <= actualAmount;
  }

  @Override
  public void updateResourcesBasedOnTroop(KingdomEntity kingdom, int amountChange) {
    ResourceEntity kingdomsGold = getResourceByResourceType(kingdom, ResourceType.GOLD);
    ResourceEntity kingdomsFood = getResourceByResourceType(kingdom, ResourceType.FOOD);
    int actualAmount = calculateActualResource(kingdom, ResourceType.GOLD);

    kingdomsGold.setAmount(actualAmount - amountChange);
    kingdomsGold.setUpdatedAt(timeService.getTime());
    BuildingEntity academy = getAcademy(kingdom);
    int level = academy.getLevel();
    kingdomsFood.setGeneration(kingdomsFood.getGeneration() + (level * defineTroopFoodFromAppProperty()));

    List<ResourceEntity> resources = Arrays.asList(kingdomsFood, kingdomsGold);
    resourceRepository.saveAll(resources);
  }

  private BuildingEntity getAcademy(KingdomEntity kingdom) {
    return kingdom.getBuildings().stream()
        .filter(b -> b.getType().equals(BuildingType.ACADEMY))
        .findFirst()
        .orElse(null);
  }

  private int defineTroopFoodFromAppProperty() {
    return Integer.parseInt(Objects.requireNonNull(env.getProperty("troop.food")));
  }

  @Override
  public boolean hasResourcesForBuilding(KingdomEntity kingdom, int amountChange) {
    int actualAmount = calculateActualResource(kingdom, ResourceType.GOLD);
    return amountChange <= actualAmount;
  }

  @Override
  public void updateResourcesByBuildingType(KingdomEntity kingdom, int amountChange) {
    ResourceEntity kingdomsGold = getResourceByResourceType(kingdom, ResourceType.GOLD);
    int actualAmount = calculateActualResource(kingdom, ResourceType.GOLD);

    kingdomsGold.setAmount(actualAmount - amountChange);
    kingdomsGold.setUpdatedAt(timeService.getTime());
    resourceRepository.save(kingdomsGold);
  }

  private Integer calculateActualResource(KingdomEntity kingdom, ResourceType resourceType) {
    ResourceEntity resource = getResourceByResourceType(kingdom, resourceType);
    Integer lastUpdatedAmount = resource.getAmount();
    Integer betweenUpdateAndActualAmount = 0;

    long updatedTime = resource.getUpdatedAt();
    long actualTime = timeService.getTime();
    int betweenUpdateAndActualTime = timeService.getTimeBetween(updatedTime, actualTime);

    if (updatedTime < actualTime) {
      betweenUpdateAndActualAmount = (betweenUpdateAndActualTime / 60 * resource.getGeneration());
    }

    return lastUpdatedAmount + betweenUpdateAndActualAmount;
  }

  @Override
  public ResourceEntity getResourceByResourceType(KingdomEntity kingdom, ResourceType resourceType) {
    return kingdom.getResources().stream()
        .filter(r -> r.getType().equals(resourceType))
        .findFirst()
        .orElse(null);
  }

  @Override
  public void saveResources(List<ResourceEntity> resources) {
    resourceRepository.saveAll(resources);
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
    ResourceEntity resource = findResourceByBuildingType(kingdom, building.getType());
    if (resource != null) {
      doResourceUpdate(kingdom, building, resource);
      if (resource != null) {
        log.info("Resource {} with ID {} will be updated. Old generation was {}, old total amount was {}",
            resource.getType(), resource.getId(), resource.getGeneration(), resource.getAmount());
      } else {
        log.warn("Resource generation was not updated when creating new building of type {}!", building.getType());
      }
      return resource;
    }

    return null;
  }

  public ResourceEntity doResourceUpdate(KingdomEntity kingdom, BuildingEntity building,
                                         ResourceEntity resourceToBeUpdated) {

    Integer newResourceGeneration = calculateNewResourceGeneration(resourceToBeUpdated, building);

    //sheduling the update to later time (when building is actually finished)
    int delay = timeService.getTimeBetween(timeService.getTime(), building.getFinishedAt()) * 1000;
    Timer timer = createNewTimer();
    ResourceTimerTask resourceTimerTask = new ResourceTimerTask(resourceToBeUpdated,
        newResourceGeneration, building, this);
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

    log.info("Resource {} with ID {} was updated. Actual generation is {}, actual amount is {}",
        resourceToBeUpdated.getType(), resourceToBeUpdated.getId(), resourceToBeUpdated.getGeneration(),
        resourceToBeUpdated.getAmount());

    return resourceToBeUpdated;
  }

  @Override
  public ResourceEntity findResourceByBuildingType(KingdomEntity kingdom, Enum buildingType) {
    return kingdom.getResources().stream()
        .filter(r -> r.getType().buildingType.equals(buildingType))
        .findFirst()
        .orElse(null);
  }

  public Integer calculateNewResourceGeneration(ResourceEntity resource, BuildingEntity building) {

    Integer defaultGeneration = Integer
        .parseInt(env.getProperty("resourceEntity." + resource.getType().resourceType));
    return resource.getGeneration() + building.getLevel() * defaultGeneration + defaultGeneration;
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
