package com.greenfoxacademy.springwebapp.resource.services;

import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
import com.greenfoxacademy.springwebapp.resource.models.dtos.ResourceListResponseDTO;
import com.greenfoxacademy.springwebapp.resource.models.dtos.ResourceResponseDTO;
import com.greenfoxacademy.springwebapp.resource.models.enums.ResourceType;
import com.greenfoxacademy.springwebapp.resource.repositories.ResourceRepository;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class ResourceServiceImpl implements ResourceService {
  private ResourceRepository resourceRepository;

  public ResourceServiceImpl(
          ResourceRepository resourceRepository) {
    this.resourceRepository = resourceRepository;
  }

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

  @Override
  public void updateResourceGeneration(KingdomEntity kingdom, Enum buildingType,
                                       Integer buildingLevel) {
    //1.finding particular kingdom´s resource to be later updated
    ResourceEntity resource = findResourceBasedOnBuildingType(kingdom,buildingType);



  }

  public ResourceEntity findResourceBasedOnBuildingType(KingdomEntity kingdom, Enum buildingType) {
    ResourceEntity resource;
    if(buildingType == BuildingType.FARM) {
      resource = kingdom.getResources().stream()
          .filter(a -> a.getType() == ResourceType.FOOD)
          .findFirst()
          .orElse(null);
    } else if (buildingType == BuildingType.MINE) {
      resource = kingdom.getResources().stream()
          .filter(a -> a.getType() == ResourceType.GOLD)
          .findFirst()
          .orElse(null);
    } else return null;

    return resource;
  }
}
