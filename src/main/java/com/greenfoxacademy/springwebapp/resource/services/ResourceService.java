package com.greenfoxacademy.springwebapp.resource.services;

import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
import com.greenfoxacademy.springwebapp.resource.models.dtos.ResourceResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ResourceService {

  boolean hasResourcesForBuilding();

  ResourceEntity saveResource(ResourceEntity resourceEntity);

  List<ResourceResponseDTO> findByKingdomId(Long id);
}
