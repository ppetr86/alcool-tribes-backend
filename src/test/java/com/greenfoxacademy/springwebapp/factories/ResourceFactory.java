package com.greenfoxacademy.springwebapp.factories;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
import com.greenfoxacademy.springwebapp.resource.models.enums.ResourceType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResourceFactory {

  public static List<ResourceEntity> createDefaultResources(KingdomEntity kingdom) {
    return Arrays.asList(
        new ResourceEntity(1L, ResourceType.GOLD, 100, 100, 999L, kingdom),
        new ResourceEntity(2L, ResourceType.FOOD, 100, 100, 999L, kingdom)
    );
  }

}