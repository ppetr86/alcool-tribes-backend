package com.greenfoxacademy.springwebapp.resource.services;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
import com.greenfoxacademy.springwebapp.resource.models.dtos.ResourceListResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ResourceService {

  boolean hasResourcesForTroop(KingdomEntity kingdom, int amountChange);

  void updateResourcesBasedOnTroop(KingdomEntity kingdom, int amountChange);

  boolean hasResourcesForBuilding(KingdomEntity kingdom, int amountChange);

  void updateResourcesByBuildings(KingdomEntity kingdom, int amountChange);

  List<ResourceEntity> createDefaultResources(KingdomEntity kingdomEntity);

  ResourceListResponseDTO convertKingdomResourcesToListResponseDTO(KingdomEntity kingdom);

  ResourceEntity updateResourceGeneration(KingdomEntity kingdom, BuildingEntity building);

  ResourceEntity findResourceByBuildingType(KingdomEntity kingdom, Enum buildingType);

}
