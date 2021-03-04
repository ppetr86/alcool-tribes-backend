package com.greenfoxacademy.springwebapp.resource.services;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingLevelDTO;
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
  public boolean hasResourcesForBuilding() {
    // TODO: hasResourcesForBuilding
    return false;
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
  

  private ResourceEntity getResourceByResourceType(KingdomEntity kingdom, ResourceType resourceType){
    return kingdom.getResources().stream()
        .filter(r -> r.getType().equals(resourceType))
        .findFirst()
        .orElse(null);
  }

  private Integer calculateActualResource(KingdomEntity kingdom, ResourceType resourceType){
    ResourceEntity resource = getResourceByResourceType(kingdom, resourceType);
    Integer lastUpdatedAmount = resource.getAmount();

    long updatedTime = resource.getUpdatedAt();
    long actualTime = timeService.getTime();
    int betweenUpdateAndActualTime = timeService.getTimeBetween(updatedTime, actualTime);

    Integer betweenUpdateAndActualAmount = (betweenUpdateAndActualTime / 60 * resource.getGeneration());

    return lastUpdatedAmount + betweenUpdateAndActualAmount;
  }
}
