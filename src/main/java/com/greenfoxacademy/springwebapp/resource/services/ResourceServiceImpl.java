package com.greenfoxacademy.springwebapp.resource.services;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
import com.greenfoxacademy.springwebapp.resource.models.dtos.ResourceListResponseDTO;
import com.greenfoxacademy.springwebapp.resource.models.dtos.ResourceResponseDTO;
import com.greenfoxacademy.springwebapp.resource.repositories.ResourceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResourceServiceImpl implements ResourceService {
  private ResourceRepository resourceRepository;

  public ResourceServiceImpl(
      ResourceRepository resourceRepository) {
    this.resourceRepository = resourceRepository;
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
  public List<ResourceEntity> findResourcesByKingdomId(Long id) {
    return resourceRepository.findAllByKingdomId(id);
  }

  @Override
  public ResourceListResponseDTO resourcesToListDTO(KingdomEntity entity) {
    return new ResourceListResponseDTO(
        entity.getResources().stream()
        .map(ResourceResponseDTO::new)
        .collect(Collectors.toList())
    );
  }
}
