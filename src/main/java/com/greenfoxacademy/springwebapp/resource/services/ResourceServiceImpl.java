package com.greenfoxacademy.springwebapp.resource.services;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
import com.greenfoxacademy.springwebapp.resource.models.dtos.ResourceListResponseDTO;
import com.greenfoxacademy.springwebapp.resource.models.dtos.ResourceResponseDTO;
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
    return true;
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

}
