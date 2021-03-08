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
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ResourceServiceImpl implements ResourceService {

  private ResourceRepository resourceRepository;
  private TimeService timeService;
  private KingdomService kingdomService;
  private Environment env;

  public boolean hasResourcesForTroop(Long kingdomId, int amountChange) {
    // TODO: has Resources For Troops creation
    KingdomEntity kingdom = kingdomService.findByID(kingdomId);

    if (updateResources(kingdom, amountChange)){
     ResourceEntity kingdomsFood = getResourceByResourceType(kingdom, ResourceType.FOOD);
     BuildingEntity academy = getAcademy(kingdom);
     int level = academy.getLevel();
     kingdomsFood.setGeneration(kingdomsFood.getGeneration() + (level * defineTroopFood()));
     saveResource(kingdomsFood);
    }
    return false;
  }

  private BuildingEntity getAcademy(KingdomEntity kingdom) {
    return kingdom.getBuildings().stream()
        .filter(b -> b.getType().equals(BuildingType.ACADEMY))
        .findFirst()
        .orElse(null);
  }

  private int defineTroopFood(){
    return Integer.parseInt(Objects.requireNonNull(env.getProperty("troop.food")));
  }

  @Override
  public boolean hasResourcesForBuilding(Long kingdomId, int amountChange) {
    KingdomEntity kingdom = kingdomService.findByID(kingdomId);
    return updateResources(kingdom, amountChange);
  }

  private boolean updateResources(KingdomEntity kingdom, int amountChange) {
    ResourceEntity kingdomsGold = getResourceByResourceType(kingdom, ResourceType.GOLD);
    int actualAmount = calculateActualResource(kingdom, ResourceType.GOLD);

    if (amountChange <= actualAmount) {
      kingdomsGold.setAmount(actualAmount - amountChange);
      kingdomsGold.setUpdatedAt(timeService.getTime());
      saveResource(kingdomsGold);
      return true;
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
    Integer betweenUpdateAndActualAmount = 0;

    long updatedTime = resource.getUpdatedAt();
    long actualTime = timeService.getTime();
    int betweenUpdateAndActualTime = (int) (actualTime - updatedTime);

    if (updatedTime < actualTime){
      betweenUpdateAndActualAmount = (betweenUpdateAndActualTime / 60 * resource.getGeneration());
    }

    return lastUpdatedAmount + betweenUpdateAndActualAmount;
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
}
