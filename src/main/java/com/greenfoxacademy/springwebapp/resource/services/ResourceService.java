package com.greenfoxacademy.springwebapp.resource.services;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
import com.greenfoxacademy.springwebapp.resource.models.dtos.ResourceListResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ResourceService {

  boolean hasResourcesForBuilding();

  List<ResourceEntity> createDefaultResources(KingdomEntity kingdomEntity);

  boolean hasResourcesForTroop();

  ResourceEntity saveResource(ResourceEntity resourceEntity);

  ResourceListResponseDTO convertKingdomResourcesToListResponseDTO(KingdomEntity kingdom);
}
