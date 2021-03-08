package com.greenfoxacademy.springwebapp.resource.services;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;

import java.util.List;

import com.greenfoxacademy.springwebapp.resource.models.dtos.ResourceListResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface ResourceService {

  boolean hasResourcesForTroop(KingdomEntity kingdom, int amountChange);

  boolean hasResourcesForBuilding(KingdomEntity kingdom, int amountChange);

  List<ResourceEntity> createDefaultResources(KingdomEntity kingdomEntity);

  ResourceEntity saveResource(ResourceEntity resourceEntity);

  ResourceListResponseDTO convertKingdomResourcesToListResponseDTO(KingdomEntity kingdom);

}
