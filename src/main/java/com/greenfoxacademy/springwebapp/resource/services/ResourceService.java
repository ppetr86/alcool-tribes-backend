package com.greenfoxacademy.springwebapp.resource.services;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
import com.greenfoxacademy.springwebapp.resource.models.dtos.ResourceListResponseDTO;
import com.greenfoxacademy.springwebapp.resource.models.enums.ResourceType;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface ResourceService {

    Integer calculateActualResource(KingdomEntity kingdom, ResourceType resourceType);


    ResourceListResponseDTO convertKingdomResourcesToListResponseDTO(KingdomEntity kingdom);


    List<ResourceEntity> createDefaultResources(KingdomEntity kingdomEntity);


    ResourceEntity findResourceByBuildingType(KingdomEntity kingdom, Enum buildingType);


    ResourceEntity getResourceByResourceType(KingdomEntity kingdom, ResourceType resourceType);


    boolean hasResourcesForBuilding(KingdomEntity kingdom, int amountChange);


    boolean hasResourcesForTroop(KingdomEntity kingdom, int amountChange);


    void saveResources(List<ResourceEntity> resources);


    void updateResourceAmount(KingdomEntity kingdom, int amountChange, ResourceType resourceType);


    ResourceEntity updateResourceGeneration(KingdomEntity kingdom, BuildingEntity building);


    void updateResourcesBasedOnTroop(KingdomEntity kingdom, int amountChange);
}
