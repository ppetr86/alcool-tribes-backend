package com.greenfoxacademy.springwebapp.resource.services;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;

import java.util.List;

public interface ResourceService {

  boolean hasResourcesForBuilding();

  List<ResourceEntity> createDefaultResources(KingdomEntity kingdomEntity);
}
