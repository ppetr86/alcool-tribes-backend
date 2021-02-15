package com.greenfoxacademy.springwebapp.resource.services;

import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
import com.greenfoxacademy.springwebapp.resource.repositories.ResourceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
