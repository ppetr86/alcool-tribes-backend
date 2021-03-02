package com.greenfoxacademy.springwebapp.resource.services;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;

import java.util.List;

import com.greenfoxacademy.springwebapp.resource.models.dtos.ResourceListResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface ResourceService {

  boolean hasResourcesForBuilding();

  List<ResourceEntity> createDefaultResources(KingdomEntity kingdomEntity);

  boolean hasResourcesForTroop();

  ResourceListResponseDTO convertKingdomResourcesToListResponseDTO(KingdomEntity kingdom);

  ResourceEntity updateResourceGeneration(KingdomEntity kingdom, BuildingEntity building);

  ResourceEntity findResourceByBuildingType(KingdomEntity kingdom, Enum buildingType);

}
