package com.greenfoxacademy.springwebapp.resource.services;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.common.services.TimeService;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.services.KingdomService;
import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
import com.greenfoxacademy.springwebapp.resource.models.dtos.ResourceListResponseDTO;
import com.greenfoxacademy.springwebapp.resource.models.dtos.ResourceResponseDTO;
import com.greenfoxacademy.springwebapp.resource.models.enums.ResourceType;
import com.greenfoxacademy.springwebapp.resource.repositories.ResourceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ResourceServiceImpl implements ResourceService {

  private ResourceRepository resourceRepository;
  private TimeService timeService;
  private KingdomService kingdomService;

  public boolean hasResourcesForTroop() {
    // TODO: has Resources For Troops creation
    return true;
  }

  @Override
  public boolean hasResourcesForBuilding(Long kingdomId, BuildingEntity building, int amountChange) {
    KingdomEntity kingdom = kingdomService.findByID(kingdomId);

    //In my opinion the better solution is using levelDTO instead of amountChange, cause this time I can check that
    //the building level will be increase or decrease. If decrease than I think the amount change not relevant.

    if (updateResourcesIfBuildTownHall(kingdom, building, amountChange)) {
      return true;
    } else if (updateResourcesIfBuildAcademy(kingdom, building, amountChange)) {
      return true;
    } else return updateResourcesIfBuildFarmOrMine(kingdom, building, amountChange);
  }

  @Override
  public List<ResourceEntity> createDefaultResources(KingdomEntity kingdomEntity) {
    return Arrays.stream(ResourceType.values())
        .map(type -> new ResourceEntity(kingdomEntity, type, 100, 10, timeService.getTime()))
        .collect(Collectors.toList());
  }

  public ResourceEntity saveResource(ResourceEntity resourceEntity) {
    return resourceRepository.save(resourceEntity);
  }

  @Override
  public ResourceListResponseDTO convertKingdomResourcesToListResponseDTO(KingdomEntity kingdom) {
    return ResourceListResponseDTO.builder()
        .withResources(kingdom.getResources().stream()
            .map(ResourceResponseDTO::new)
            .collect(Collectors.toList()))
        .build();
  }

  // non relevant method right now
  private void updateResources(Long kingdomId, ResourceType resourceType, int amountChange) {
    //get kingdom just in this method
    KingdomEntity kingdom = kingdomService.findByID(kingdomId);

    ResourceEntity resource = getResourceByResourceType(kingdom, resourceType);

    assert resource != null;
    if (amountChange < 0) resource.setAmount(resource.getAmount() - amountChange);
    if (0 < amountChange) resource.setAmount(resource.getAmount() + amountChange);
    resource.setUpdatedAt(timeService.getTime());

    saveResource(resource);
  }

  private boolean updateResourcesIfBuildTownHall(KingdomEntity kingdom, BuildingEntity building, int amountChange) {
    ResourceEntity kingdomsGold = getResourceByResourceType(kingdom, ResourceType.GOLD);
    int actualAmount = calculateActualResource(kingdom, ResourceType.GOLD);

    if (building.getType().equals(BuildingType.TOWNHALL)) {
      if (amountChange <= actualAmount) {
        kingdomsGold.setAmount(actualAmount - amountChange);
        saveResource(kingdomsGold);
        return true;
      }
    }
    return false;
  }

  private boolean updateResourcesIfBuildAcademy(KingdomEntity kingdom, BuildingEntity building, int amountChange) {
    ResourceEntity kingdomsGold = getResourceByResourceType(kingdom, ResourceType.GOLD);
    int actualAmount = calculateActualResource(kingdom, ResourceType.GOLD);

    if (building.getType().equals(BuildingType.ACADEMY)) {
      if (amountChange <= actualAmount) {
        kingdomsGold.setAmount(actualAmount - amountChange);
        saveResource(kingdomsGold);
        return true;
      }
    }
    return false;
  }

  private boolean updateResourcesIfBuildFarmOrMine(KingdomEntity kingdom, BuildingEntity building, int amountChange) {
    ResourceEntity kingdomsGold = getResourceByResourceType(kingdom, ResourceType.GOLD);
    int actualAmount = calculateActualResource(kingdom, ResourceType.GOLD);

    if (building.getType().equals(BuildingType.FARM) || building.getType().equals(BuildingType.MINE)) {
      if (amountChange <= actualAmount) {
        kingdomsGold.setAmount(actualAmount - amountChange);
        saveResource(kingdomsGold);
        return true;
      }
    }
    return false;
  }

  private ResourceEntity getResourceByResourceType(KingdomEntity kingdom, ResourceType resourceType) {
    return kingdom.getResources().stream()
        .filter(r -> r.getType().equals(resourceType))
        .findFirst()
        .orElse(null);
  }

  private Integer calculateActualResource(KingdomEntity kingdom, ResourceType resourceType) {
    ResourceEntity resource = getResourceByResourceType(kingdom, resourceType);
    Integer lastUpdatedAmount = resource.getAmount();

    long updatedTime = resource.getUpdatedAt();
    long actualTime = timeService.getTime();
    int betweenUpdateAndActualTime = timeService.getTimeBetween(updatedTime, actualTime);

    Integer betweenUpdateAndActualAmount = (betweenUpdateAndActualTime / 60 * resource.getGeneration());

    return lastUpdatedAmount + betweenUpdateAndActualAmount;
  }
}
