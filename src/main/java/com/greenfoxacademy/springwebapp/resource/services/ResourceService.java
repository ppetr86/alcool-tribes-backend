package com.greenfoxacademy.springwebapp.resource.services;

import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ResourceService {

  boolean hasResourcesForBuilding();

  ResourceEntity saveResource(ResourceEntity resourceEntity);

  List<ResourceEntity> findByKingdomId(Long id);
}
