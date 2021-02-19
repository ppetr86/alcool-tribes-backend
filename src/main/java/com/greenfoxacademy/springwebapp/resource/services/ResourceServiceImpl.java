package com.greenfoxacademy.springwebapp.resource.services;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
import com.greenfoxacademy.springwebapp.resource.models.enums.ResourceType;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResourceServiceImpl implements ResourceService {

  @Override
  public boolean hasResourcesForBuilding() {
    // TODO: hasResourcesForBuilding
    return false;
  }

  @Override
  public List<ResourceEntity> createDefaultResources(KingdomEntity kingdomEntity) {
    return Arrays.stream(ResourceType.values())
        .map(type -> new ResourceEntity(kingdomEntity, type, 100))
        .collect(Collectors.toList());
  }
}
